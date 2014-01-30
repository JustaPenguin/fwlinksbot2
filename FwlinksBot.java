import org.jibble.pircbot.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FwlinksBot extends PircBot
{

	// servers
	private static Server[] servers = {
		new Server("irc.chronic-dev.org", 6667, new String[] {"#cj-case", "#iH8sn0w"}),
		new Server("iphun.osx86.hu", 6667, new String[] {"#ios"}),
		new Server("irc.saurik.com", 6667, new String[] {"#teambacon"}),
		new Server("irc.freenode.net", 6667, new String[] {"#jailbreakqa", "#openjailbreak", "#testfwlinks"})
	};

	private static FwlinksBot[] bots = new FwlinksBot[servers.length];

	public FwlinksBot()
	{
		this.setName("fwlinksbot");
		this.setLogin("fwlinks");
		this.setVersion("fwlinksbot");
	} // FwlinksBot

	public static void printLog(String message)
	{
        Date date = new Date();
        DateFormat dateFormat = 
            new SimpleDateFormat("[HH:mm:ss] ");
        System.out.println(dateFormat.format(date) + message);
	}

	public static void main(String[] args) throws Exception 
	{
		// connect to each server
		for(int botIndex = 0; botIndex < servers.length; botIndex++)
		{
			FwlinksBot bot = new FwlinksBot();
			bots[botIndex] = bot;
			try 
			{
				// configuration
				bot.setVerbose(false);
				bot.setAutoNickChange(true);
				printLog("Connecting to server: " + servers[botIndex].getAddress());
				bot.connect(servers[botIndex].getAddress(), servers[botIndex].getPort());

				for(String channel : servers[botIndex].getChannels())
					bot.joinChannel(channel);
				
			} // try
			catch (Exception exception) 
			{
				System.out.println(exception);
			} // catch
		} // for
	} // main

	public void errorMessage(String channel, String sender, String message)
	{
		printLog("error: " + sender + ": " + message);
		sendMessage(channel, sender + ": " + message);
	} // errorMessage

	protected void onInvite(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String channel)
	{
		printLog("Invite to channel " + channel + " received. Joining...");
		joinChannel(channel);
		sendMessage(channel, sourceNick + ": thanks for the invite!");
	} // onInvite

	protected void onMessage(String channel, String sender, String login, String hostname, String message)
	{
		// parse args into an array
		String[] args = message.split(" ");

		APIRequest api = new APIRequest(this, channel, sender);

		switch(args[0].toLowerCase()) {

			case "!help":
				this.sendMessage(channel, sender + ": view my commands here: http://api.ios.icj.me/docs/fwlinksbot");
			break;

			case "!fw":
			case "!firmware":
				api.firmware(args);
			break;

			case "!redsn0w":
			case "!rs":
				api.redsn0w(args);
			break;

			case "!itunes":
			case "!it":
				api.iTunes(args);
			break;

			case "!tss":
				api.tss(args);
			break;

			case "!shsh":
				api.shsh(args);
			break;

			case "!pwnagetool":
			case "!pt":
				api.pwnagetool(args);
			break;

			default:
			break;

		} // switch

		return;

	} // onMessage

}
