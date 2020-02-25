package adstech.vn.quotationbackoffice.util;

import java.io.IOException;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.chat.ChatPostMessageRequest;
import com.github.seratch.jslack.api.methods.request.im.ImOpenRequest;
import com.github.seratch.jslack.api.methods.request.users.UsersLookupByEmailRequest;
import com.github.seratch.jslack.api.methods.response.channels.UsersLookupByEmailResponse;
import com.github.seratch.jslack.api.methods.response.im.ImOpenResponse;

public class SlackUtil {

	static String token = "xoxb-513115537076-604354155329-4S5DSue86d9TCeXGrGkUFlKr";
	static Slack slack = Slack.getInstance();

	public static void sendDirectMsg(String userEmail, String msg) {
		try {
			UsersLookupByEmailResponse response = slack.methods()
					.usersLookupByEmail(UsersLookupByEmailRequest.builder().token(token).email(userEmail).build());
			if (response.isOk()) {
				String userId = response.getUser().getId();
				ImOpenResponse openResponse = slack.methods()
						.imOpen(ImOpenRequest.builder().token(token).user(userId).build());

				if (openResponse.isOk()) {
					String channelId = openResponse.getChannel().getId();

					slack.methods().chatPostMessage(ChatPostMessageRequest.builder().token(token).channel(channelId)
							.username("Báo giá 5 phút!").iconEmoji(":clipboard:").asUser(false).text(msg).build());
				}
			}
		} catch (IOException | SlackApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
