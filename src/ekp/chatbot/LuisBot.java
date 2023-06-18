package ekp.chatbot;

import java.util.ArrayList;
import java.util.List;

import legion.data.search.SearchOperation;
import legion.luis.AddUtteranceObj;
import legion.luis.LuisEntity;
import legion.luis.LuisIntent;
import legion.luis.LuisResult;
import legion.util.DataFO;

public class LuisBot implements SimpleBot{
	// -------------------------------------------------------------------------------
	private double confirmIntentThreshold;
	private BotChatStatus chatStatus;
	private List<Line> lineList;

	public LuisBot() {
		confirmIntentThreshold = 0.67;
		chatStatus = BotChatStatus.DEFAULT;
		lineList = new ArrayList<>();
	}

	// -------------------------------------------------------------------------------
	@Override
	public String[] getResponseNew(String _utterance) {
		String r = getResponse(_utterance);
		return new String[] {r};
	}
	
	
	private String getResponse(String _utterance) {
		BotChatStatus chatStatusCache = chatStatus;

		// record
		lineList.add(new Line(chatStatusCache, BotSpeaker.USER, _utterance));

		String result = "";
		try {
			switch (chatStatus) {
			case DEFAULT:
				result = getDefaultResponse(_utterance);
				break;
			case CONFIRM_INTENT:
				try {
					int intentIndex = Integer.parseInt(_utterance);
					System.out.println("intentIndex: " + intentIndex);
					result = addIntentUtterance(EkpIntent.get(intentIndex));
				} catch (Throwable e) {
					chatStatus = BotChatStatus.DEFAULT;
					System.out.println("test throwable ");
					System.out.println("_utterance: " + _utterance);
					result = getResponse(_utterance);
				}
				break;
			}
		} catch (Throwable e) {
			result = "There is something error. Please report to the system manager.";
		}
		lineList.add(new Line(chatStatusCache, BotSpeaker.BOT, result));
		return result;
	}

	private String getDefaultResponse(String _utterance) throws Exception {
		System.out.println("Start::getDefaultResponse");

		// understanding by EkpLuisProxy
		LuisResult lr = EkpLuisProxy.getInstance().understanding(_utterance);

		String result = "";
		/* confirm intent when score < threshold */
		System.out.println("lr.getTopScoringIntentName(): " + lr.getTopScoringIntentName());
		System.out.println("lr.getScore(): " + lr.getScore());
		if (lr.getScore() < confirmIntentThreshold) {
			chatStatus = BotChatStatus.CONFIRM_INTENT; // chatStatus->confirmIntent
			return getToConfirmIntentResponse(_utterance); // to confirm intent.
		}

		// 取出entities
		List<LuisEntity> entities = lr.getEntities();
		EkpIntent ekpIntent = EkpIntent.get(lr.getTopScoringIntentName());
		switch (ekpIntent) {
		// TODO
//		case EK_DEF:
//			System.out.println("entities.size(): " + entities.size());
////				String e = entities.get(0).getName();
//			if (entities.size() > 0) {
//				String e = entities.get(0).getResolution();
//				KeywordDef kwd = MtnServiceFacade.getInstance().loadKeywordDefByKeyword(e);
//				result = kwd.getDef();
//			} else {
//				result = "Sorry, that's not my forte. Let me google it for you.";
//				result += System.lineSeparator();
//				result += GoogleSearchProxy.getInstance().getFirstResultUrl(_utterance);
//			}
//			break;
//		case EK_SEARCH_DOC:
//			result = "EK_SEARCH_DOC TODO";
//
//			/* 找到projectID */
//			boolean hasProjectId = false;
//			// TODO
////				String[] strs = _utterance.split(" ");
////				String projectId = "";
////				for(String str:strs) {
////					str = str.toUpperCase();
////					if (str.matches("[A-Z0-9]{6}")) {
////						projectId = str;
////						break;
////					}
////				}
////				System.out.println("projectId: " + projectId);
////				if (!DataFO.isEmptyString(projectId)) {
////					hasProjectId = true;
////				}
//
//			if (entities.size() > 0) {
//				String r = entities.get(0).getResolution();
//				EkpEntity e = EkpEntity.get(r);
//				switch (e) {
//				case RFI:
//					SearchOperation<RfiSearchParam, Rfi> param = new SearchOperation<>();
////						if (hasProjectId) {
////							param.addCondition(RfiSearchParam.PJ_CODE, CompareOp.EQUAL, projectId);
////						}
//					List<Rfi> rfiList = BidServiceFacade.getInstance().searchRfi(param).getResultList();
//					result = rfiList.stream().map(rfi -> rfi.getSerialNo()).collect(Collectors.joining(","));
//					break;
//				default:
//					result = "not implemented yet.";
//					break;
//				}
//			} else {
//				result = "Please tell me what you want kine of documnet you want to search.";
//				break;
//			}
//			// TODO
//			break;
		case NONE:
		default:
			result = "Uh huh";
			break;
		}
		System.out.println("End::getDefaultResponse");
		return result;
	}

	private String utteranceToBeLabeled;

	private String getToConfirmIntentResponse(String _utterance) throws Exception {
		utteranceToBeLabeled = _utterance;
		String result = "";
		result += "Sorry, I didn't get it. Which of following is your intent?";
		for (LuisIntent intent : EkpLuisProxy.getInstance().getAllIntents()) {
			EkpIntent ekpIntent = EkpIntent.get(intent.getName());
//				if (ekpIntent.equals(EkpIntent.NONE))
//					continue;
			result += System.lineSeparator() + ekpIntent.getIndex() + "." + ekpIntent.getDescription();
		}
		return result;
	}

	private String addIntentUtterance(EkpIntent _intent) throws Exception {
		System.out.println("Start::addIntentUtterance");
		System.out.println("utteranceToBeLabeled: " + utteranceToBeLabeled);
		System.out.println("_intent: " + _intent);
		if (DataFO.isEmptyString(utteranceToBeLabeled))
			throw new Exception();
		System.out.println("utteranceToBeLabeled: " + utteranceToBeLabeled);
		AddUtteranceObj obj = new AddUtteranceObj(utteranceToBeLabeled, _intent.getName());
		System.out.println("test 1");
		boolean add = EkpLuisProxy.getInstance().addUtterance(obj);
		if (!add)
			throw new Exception();
		System.out.println("test 2");
		boolean train = EkpLuisProxy.getInstance().train();
		if (!train)
			throw new Exception();
		System.out.println("test 3");
		boolean publish = EkpLuisProxy.getInstance().publish();
		if (!publish)
			throw new Exception();
		System.out.println("test 4");
		utteranceToBeLabeled = "";
		chatStatus = BotChatStatus.DEFAULT; // chatStatus -> DEFAULT
		System.out.println("End::addIntentUtterance");
		return "OK, note it!";
	}
}
