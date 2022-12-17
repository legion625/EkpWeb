package ekp.web.control.zk.chatbot;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;

import ekp.chatbot.SimpleBot;
import ekp.DebugLogMark;
import ekp.chatbot.LuisBot;
import ekp.chatbot.RuleBasedBot;
import legion.aspect.AspectBus;
import legion.util.LogUtil;
import legion.web.zk.ZkMsgBox;
import legion.web.zk.ZkNotification;

public class ChatbotComposer extends SelectorComposer<Component> {
	private Logger log = LoggerFactory.getLogger(ChatbotComposer.class);
//	private Logger log = LoggerFactory.getLogger(DebugLogMark.class); 

	// -------------------------------------------------------------------------------
	public final static String URI = "/consultantbot/bot.zul";

	// -------------------------------------------------------------------------------
	@Wire
	private Div divView;
	@Wire
	private Textbox txbInput;
	@Wire
	private Button btnSend;
	@Wire
	private Timer timer;

	private String botName = "BOT:";
	private String userName = "USER:";

	private boolean complete = false;
//	private String response = "";
	private String[] response = null;

	private SimpleBot bot;

	// -------------------------------------------------------------------------------
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			int idx = Integer.parseInt(Executions.getCurrent().getParameter("idx"));
			log.debug("idx: {}", idx);
			switch (idx) {
			case 1: {
				bot = new RuleBasedBot();
				break;
			}
			case 2: {
				bot = new LuisBot();
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + idx);
			}
			timer.stop();
		} catch (Throwable e) {
			LogUtil.log(e, Level.ERROR);
		}
	}

//	@Listen(Events.ON_CLICK + "=#btnSend; " + Events.ON_OK + "=#txbInput")
	@Listen(Events.ON_CLICK + "=#btnSend")
	public void btnSend_clicked() {
		sendMsg();
	}

	@Listen(Events.ON_OK + "=#txbInput")
	public void txbInput_ok() {
		ZkNotification.info("test on OK");
		Events.echoEvent(new Event(Events.ON_CHANGING, txbInput));
		sendMsg();
	}

	private void sendMsg() {
		log.debug("sendMsg");
		String utterance = txbInput.getValue();
//		txbInput.val
		log.debug("txbInput.getValue(): {}", txbInput.getValue());
		log.debug("txbInput.getRawValue(): {}", txbInput.getRawValue());
		log.debug("txbInput.getText(): {}", txbInput.getText());
		log.debug("txbInput.getRawText(): {}", txbInput.getRawText());
		log.debug("txbInput.getWidgetAttributeNames(): {}", txbInput.getWidgetAttributeNames());

//		txbInput.getcli

		if (utterance.isEmpty()) {
			log.debug("utterance is empty, return.");
			return;
		}

		log.debug("test 1");

		btnSend.setDisabled(true);

		divView.appendChild(new Line(userName, LineSpeakerType.USER).div);
		Div div = new Line(utterance, LineSpeakerType.MSG).div;
		divView.appendChild(div);
		Clients.scrollIntoView(div);
		clearInputText();

		log.debug("test 2");

		Thread thread = new Thread() {
			@Override
			public void run() {
				super.run();
				log.debug("test 5");
				try {
					// sleep(100);
					// TODO
//					System.out.println("bot.hashCode(): " + bot.hashCode());
					log.debug("bot.hashCode(): {}", bot.hashCode());
//					response = bot.getResponse(utterance);
					response = bot.getResponseNew(utterance);
					complete = true;

					log.debug("test 6");

					// } catch (InterruptedException e) {
					// e.printStackTrace();
				} catch (Throwable e) {
					LogUtil.log(log, e);
//					e.printStackTrace();
//					response = "something error!!";
					response = new String[] { "something error!!" };
					complete = true;
				}
			}
		};

		log.debug("test 3");
		timer.start();
		thread.start();
		log.debug("test 4");
	}

	@Listen(Events.ON_CLICK + "=#btnClear")
	public void btnClear_clicked() {
		clearInputText();
	}

	private void clearInputText() {
		txbInput.setValue("");
	}

	@Listen(Events.ON_TIMER + "=#timer")
	public void timer_onTimer() {
		if (complete) {
			timer.stop();
			divView.appendChild(new Line(botName, LineSpeakerType.USER).div);

			for (String r : response) {
				Div div = new Line(r, LineSpeakerType.MSG).div;
				divView.appendChild(div);
				Clients.scrollIntoView(div);
			}

			complete = false;
//			response = "";
			response = null;
			btnSend.setDisabled(false);
//			Clients.scrollIntoView(div);
		}
	}

	// -------------------------------------------------------------------------------
	private class Line {
		private Div div;
		private Label lb;

		private Line(String _msg, LineSpeakerType _speakerType) {
			div = new Div();
			lb = new Label(_msg);
			div.appendChild(lb);

			switch (_speakerType) {
			case USER:
				div.setStyle("text-align:left;font-weight:bold;padding-top:5px");
				break;
			case MSG:
				div.setStyle("text-align:left");
				lb.setStyle("padding-left:20px");
				break;
			}
		}
	}

	private enum LineSpeakerType {
		USER, MSG;
	}

}
