package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.requests.EmailNotificationRequest;
import africa.semicolon.promeescuous.dtos.requests.Recipient;
import africa.semicolon.promeescuous.dtos.responses.EmailNotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
public class MailServiceTest {
    @Autowired
    private MailService mailService;
    private EmailNotificationRequest request;

    @BeforeEach
    public void setUp(){
        String recipientEmail = "joyscholar077@gmail.com";
        String message =getTemplate();
        String subject = "test email";

        request = new EmailNotificationRequest();
        request.setMailContent(message);
        request.setRecipients(List.of(new Recipient(recipientEmail)));
        request.setSubject(subject);
    }
    @Test
    public void testThatEmailSendingWorks(){
        EmailNotificationResponse emailNotificationResponse = mailService.send(request);
        log.info("response:: {}", emailNotificationResponse);
        assertNotNull(emailNotificationResponse);
    }


    private static String getTemplate(){
       return  """
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                </head>
                <body>
                <style>
                        p{
                            font-family: Arial;
                           \s
                        }
                                
                        .text{
                            width: 300px;
                            line-height: 20px;
                            font-size: 12px;
                        }
                                
                        .activate{
                            background-color: rgb(0, 149, 255);
                            color: white;
                            border: none;
                            padding-left: 30px;
                            padding-right: 30px;
                            padding-top: 15px;
                            padding-bottom: 15px;
                            border-radius: 22px;
                            transition: all 0.3s;
                            margin-left: 80px;
                            font-family: Arial;
                            font-weight: bold;
                        }
                                
                        .activate:hover{
                            cursor: pointer;
                            background-color: white;
                            color: rgb(0, 149, 255);
                            border: 1px solid rgb(0, 149, 255);
                        }
                                
                        .activate:active{
                            background-color: rgb(0, 149, 255);
                            color: white;
                        }
                                
                        .main-container{
                            position: absolute;
                            top: 200px;
                            left: 200px;
                            width: 300px;
                            align-self: center;
                        }
                       \s
                        .logo{
                            width: 200px;
                            margin-left: 80px;
                            border:none;
                        }
                                
                        .social-links{
                            margin-left: 70px;
                        }
                                
                        .bottom-section{
                            margin-left: 50px;
                        }
                                
                        .social-links img{
                            width: 25px;
                            margin-top: 20px;
                            margin-right: 0;
                            margin-top: 0;
                        }
                                
                        .social-links-description{
                            font-size: 14;
                            color: #969696;
                            margin-left: 45px;
                            margin-bottom: 10px;
                        }
                    </style>
                                
                <div class="main-container">
                    <img src="https://res.cloudinary.com/dlvi5kpsr/image/upload/v1691484327/promiscuous/assets/happy-couple_teaokd.jpg" alt="" class="logo">
                    <div class="bottom-section">
                        <p class="text">
                            Welcome to promiscuous.org, we are pleased to have you,
                            please click on the button below to activate your account.
                        </p>
                        <a href=\\"%s\\">
                            <button type="button" class="activate">
                                Activate
                            </button>
                        </a>
                                
                        <p class="social-links-description">join our social communities:</p>
                        <div class="social-links">
                            <a href="https://www.facebook.com">
                                <img src="https://res.cloudinary.com/dlvi5kpsr/image/upload/v1691484325/promiscuous/assets/facebook_dsmtwl.png" alt="" class="facebook-icon">
                            </a>
                            <a href="https://www.twitter.com">
                                <img src="https://res.cloudinary.com/dlvi5kpsr/image/upload/v1691484325/promiscuous/assets/twitter_ryjyre.png" alt="" class="twitter icon">
                            </a>
                            <a href="https://www.instagram.com">
                                <img src="https://res.cloudinary.com/dlvi5kpsr/image/upload/v1691484325/promiscuous/assets/instagram_zlxeaf.png" alt="" class="instagram-icon">
                            </a>
                            <a href="https://www.linkedin.com">
                                <img src="https://res.cloudinary.com/dlvi5kpsr/image/upload/v1691484325/promiscuous/assets/linkedin_hkemvv.png" alt="" class="linkedin-icon">
                            </a>
                        </div>
                    </div>
                </div>
                </body>
                </html>""";
    }
}
