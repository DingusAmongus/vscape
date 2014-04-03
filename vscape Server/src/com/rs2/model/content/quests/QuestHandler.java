package com.rs2.model.content.quests;

//import com.rs2.model.content.DialogueManager;
//import com.rs2.model.content.DialogueManager.Dialogue;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.quests.CooksAssistant;
import com.rs2.model.players.item.*;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.PlayerSave;

/**
 * @date 1-jun-2011
 * @author Satan666
 */
public class QuestHandler {

    public static final int[] QUEST_IDS = {
        8145, 8146, 8147, 8148, 8149, 8150, 8151, 8152, 8153, 8154,
        8155, 8156, 8157, 8158, 8159, 8160, 8161, 8162, 8163, 8164,
        8165, 8166, 8167, 8168, 8169, 8170, 8171, 8172, 8173, 8174,
        8175, 8176, 8177, 8178, 8179, 8180, 8181, 8182, 8183, 8184,
        8185, 8186, 8187, 8188, 8189, 8190, 8191, 8192, 8193, 8194,
        8195, 12144, 12150, 12151, 12152, 12153, 12154, 12155, 12146
    };
    public static final int QUEST_INTERFACE = 8134;
    //private static Map<String, Quest> quests = new HashMap<String, Quest>();
    private static Quest[] quests = new Quest[2];
    
    public static Quest cook = new CooksAssistant();
    public static Quest knightsword = new TheKnightsSword();
    
    
    public static void init() {
        System.out.println("Loading quests...");
        quests[0]=cook;
        quests[1]=knightsword;
        
        System.out.println("Loaded " + quests.length + " quests.");
    }
    
    public static void initPlayer(Player player){
        player.setQuestStage(0, 0); //Initialize cooks assistant value
        player.setQuestStage(1, 0); //Initialize the knights sword value
        
        player.sendQuestTab(); //Makes the quest log empty except implemented quests
        
        PlayerSave.loadQuests(player); //loads quest progress from Username.txt, sets variables
        
        quests[0].sendQuestTabStatus(player); //sends the quest log entry to the client
        quests[1].sendQuestTabStatus(player); 
    }
    
    public static Quest[] getQuests(){
    	return quests;
    }

    public static int getTotalQuests() {
        return quests.length;
    }

    public static void startQuest(Player player, int questID) {
        Quest quest = quests[questID];
        if (quest == null) {
            return;
        }
        if (!quest.canDoQuest(player)) {
            player.getActionSender().removeInterfaces();
            player.getActionSender().sendMessage("You can't do this quest yet.");
            return;
        }
        if (player.getQuestStage(quest.getQuestID()) == 0) {
            resetInterface(player);
            quest.startQuest(player);
        }
    }

    public static void completeQuest(Player player, int questID) {
        Quest quest = quests[questID];
        if (quest == null) {
            return;
        }
        resetInterface(player);
        quest.completeQuest(player);
    }

    public static void handleQuestButtons(Player player, int button) {
        Quest quest = null;
        switch (button) {
            case 28165: //Cooks Assistant Button
                quest = quests[0];
                quest.showInterface(player);
                break;
        	case 28178: //The Knights Sword Button
        		quest = quests[1];
        		quest.showInterface(player);
        		break;
        }

        if (quest != null) {
            resetInterface(player);
            quest.sendQuestRequirements(player);
        }
    }

    public static void resetInterface(Player player) {
        for (int i = 0; i < QUEST_IDS.length; i++) {
            player.getActionSender().sendString("", QUEST_IDS[i]);
        }
    }

}