package de.teampb.soco.llm.guitester.view.chat;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.teampb.soco.llm.guitester.service.ChatService;
import de.teampb.soco.llm.guitester.template.MainLayout;
import io.github.ollama4j.models.chat.OllamaChatMessageRole;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * The main view contains a text field for getting the user name and a button
 * that shows a greeting message in a notification.
 */
@Route(value = "chat/image",layout = MainLayout.class)
@PageTitle("Chat about Images (requires image classification capable model)")
public class ImageChatView extends VerticalLayout {

    private static final Logger LOG = LoggerFactory.getLogger(ImageChatView.class);

    private MessageList chat;
    private MessageInput input;
    private List<MessageListItem> chatEntries  = new ArrayList<>();

    private List<byte[]> imagesForMessage = new ArrayList<>();

    @Inject
    private ChatService chatService;

    public ImageChatView() {

        H2 header = new H2("Chat about images");

        chat = new MessageList();
        input = new MessageInput();
        chat.setItems(new MessageListItem[0]);
        input.addSubmitListener(this::onSubmit);
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.addSucceededListener(e -> {
            String fileName = e.getFileName();
            InputStream fileInputStream = buffer.getInputStream(fileName);
            addFileToImages(fileInputStream);
        });
        Button clearButton = new Button("Clear Chat");
        clearButton.addClickListener(c -> {
            getUI().ifPresent(ui -> ui.access(
                    this::clearChat
            ));
        });

        HorizontalLayout uploadHorizontalLayout = new HorizontalLayout(upload,clearButton);
        this.setPadding(true); // Leave some white space
        this.setHeightFull(); // We maximize to window
        this.setWidthFull();
        chat.setSizeFull(); // Chat takes most of the space
        input.setWidthFull(); // Full width only
        chat.setMaxWidth("1200px"); // Limit the width
        //input.setMaxWidth("1100px");
        uploadHorizontalLayout.setMaxWidth("1200px");
        add(header, chat, input, uploadHorizontalLayout);
    }

    private void addFileToImages(InputStream fileInputStream) {
        ByteArrayOutputStream bos  = new ByteArrayOutputStream();
        try{
            int nRead;
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            final byte[] targetArray = bos.toByteArray();
            this.imagesForMessage.add(targetArray);
        }
        catch(IOException ioException){
            LOG.error("Could not parse uploaded image!", ioException);
        }
    }

    private void onSubmit(MessageInput.SubmitEvent submitEvent) {
        MessageListItem question = new MessageListItem(submitEvent.getValue(), Instant.now(), OllamaChatMessageRole.USER.getRoleName());
        question.setUserAbbreviation("US");
        question.setUserColorIndex(1);
        chatEntries.add(question);
        MessageListItem answer = new MessageListItem("Thinking.....", Instant.now(), OllamaChatMessageRole.ASSISTANT.getRoleName());
        chatEntries.add(answer);
        answer.setUserAbbreviation("AS");
        answer.setUserColorIndex(2);
        chat.setItems(chatEntries);
        Thread t = new Thread(()-> {
            chatService.sendChatWithImages(submitEvent.getValue(), imagesForMessage ,
                    (s)-> {
                        getUI().ifPresent(ui -> ui.access(
                                () -> {
                                    answer.setText(s);
                                }
                        ));
                    });
        });
        t.start();
    }

    public void clearChat(){
        chatEntries.clear();
        chat.setItems(chatEntries);
        imagesForMessage.clear();
    }

}