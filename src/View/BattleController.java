package View;

import Player.Player;
import Player.하비;
import Player.거스;

import javax.swing.*;
import java.util.*;
import javax.swing.Timer;
import View.BattleView;

public class BattleController {

    private enum TurnState {
        USER_SELECT_ATTACKER,
        USER_SELECT_TARGET,        // 일반 공격 타겟 선택
        USER_SELECT_WEAPON_TARGET, // 무기 공격 타겟 선택
        COMPUTER_TURN,
        GAME_OVER
    }

    private TurnState state = TurnState.USER_SELECT_ATTACKER;

    private final Player[]   ps;
    private final BattleView view;
    private final Random     random = new Random();

    private static final int[] LEFT_PS_IDX  = {0, 2, 4};
    private static final int[] RIGHT_PS_IDX = {1, 3, 5};

    private Player selectedAttacker = null;
    private int    selectedPanelIdx = -1; // 공격자 패널 인덱스 저장

    public BattleController(Player[] ps, BattleView view) {
        this.ps   = ps;
        this.view = view;
        registerButtons();
        enterUserSelectAttacker();
    }
   
    private void isDanger(Player target, BattleView view, boolean isUserTeam, Runnable onComplete) {
        if (target.getHp() <= 100 && target.isAlive()) {
            if (isUserTeam) {
                view.skillBoard(target, ps, onComplete);
            } else {
                autoUseSkill(target, onComplete);
            }
        } else {
            onComplete.run();
        }
    }
    
    private void autoUseSkill(Player target, Runnable onComplete) {
        String[] skills = target.getSkillNames();
        List<Integer> available = new ArrayList<>();
        for (int i = 0; i < skills.length; i++) {
            if (!target.isSkillUsed(i)) available.add(i);
        }
        if (!available.isEmpty()) {
            int skillIdx = available.get(random.nextInt(available.size()));
            target.useSkill(skillIdx, view);
            view.appendLog("[JOJA] " + target.getName() + " 스킬 [" + skills[skillIdx] + "] 자동 발동!", target.team);
            view.updateAllTeams(ps);
        }
        onComplete.run();
    }

    // ㅡㅡㅡㅡㅡㅡ 버튼/클릭 콜백 등록 ㅡㅡㅡㅡㅡㅡ

    private void registerButtons() {
        for (int i = 0; i < 3; i++) {
            final int panelIdx = i;
            view.setOnAttackBtnClicked(i,  () -> onAttackBtnClicked(panelIdx));
            view.setOnWeaponBtnClicked(i,  () -> onWeaponBtnClicked(panelIdx));
        }

        for (int i = 0; i < 3; i++) {
            final int idx = i;
            view.setOnUnitClicked(true,  i, () -> onTargetClicked(LEFT_PS_IDX[idx]));
            view.setOnUnitClicked(false, i, () -> onTargetClicked(RIGHT_PS_IDX[idx]));
        }
    }

    // ㅡㅡㅡㅡㅡㅡ 상태 전환 ㅡㅡㅡㅡㅡㅡ

    private void enterUserSelectAttacker() {
        state = TurnState.USER_SELECT_ATTACKER;
        selectedAttacker = null;
        selectedPanelIdx = -1;
        view.clearAllHighlights();
        view.appendLog("[USER] 공격할 캐릭터의 [공격] 버튼을 누르세요.", "reset");
    }

    private void enterUserSelectTarget(int panelIdx) {
        state = TurnState.USER_SELECT_TARGET;
        selectedPanelIdx = panelIdx;
        view.highlightAsAttacker(panelIdx);
        view.highlightAllEnemies();
        view.appendLog("[USER] 공격할 대상 캐릭터를 클릭하세요.", "reset");
    }

    private void enterUserSelectWeaponTarget(int panelIdx) {
        state = TurnState.USER_SELECT_WEAPON_TARGET;
        selectedPanelIdx = panelIdx;
        view.highlightAsAttacker(panelIdx);
        view.highlightAllEnemies();
        view.appendLog("[USER] 무기 공격할 대상 캐릭터를 클릭하세요.", "reset");
    }

    private void enterUserSelectAlly(int panelIdx) {
        state = TurnState.USER_SELECT_TARGET;
        selectedPanelIdx = panelIdx;
        view.highlightAsAttacker(panelIdx);
        view.highlightAllAllies(panelIdx);
        view.appendLog("[USER] 스킬을 사용할 아군 캐릭터를 클릭하세요.", "reset");
    }

    private void enterComputerTurn() {
        state = TurnState.COMPUTER_TURN;
        view.clearAllHighlights();
        view.appendLog("[COMPUTER] 자동 공격 중...", "reset");

        Timer timer = new Timer(1000, e -> {
            ((Timer) e.getSource()).stop();
            if (state == TurnState.COMPUTER_TURN) executeComputerAttack();
        });
        timer.start();
    }

    // ㅡㅡㅡㅡㅡㅡ 이벤트 핸들러 ㅡㅡㅡㅡㅡㅡ

    private void onAttackBtnClicked(int panelIdx) {
        if (state != TurnState.USER_SELECT_ATTACKER) return;

        Player attacker = ps[LEFT_PS_IDX[panelIdx]];
        if (!attacker.isAlive()) return;

        selectedAttacker = attacker;
        

        if (isSupporter(attacker) && !isDealerMode(attacker)) {
            enterUserSelectAlly(panelIdx);
        } else {
            enterUserSelectTarget(panelIdx);
        }
    }

    private void onWeaponBtnClicked(int panelIdx) {
        if (state != TurnState.USER_SELECT_ATTACKER) return;

        Player attacker = ps[LEFT_PS_IDX[panelIdx]];
        if (!attacker.isAlive()) return;
        if (attacker.getWeapon() == null) return;

        selectedAttacker = attacker;
        enterUserSelectWeaponTarget(panelIdx);
    }

    private void onTargetClicked(int psIdx) {
        Player target = ps[psIdx];
        if (!target.isAlive()) return;

        boolean isEnemy = isRightTeam(psIdx);

        if (state == TurnState.USER_SELECT_WEAPON_TARGET) {
            if (!isEnemy) return; // 아군 클릭 무시
            executeUserWeaponAttack(selectedAttacker, target);

        } else if (state == TurnState.USER_SELECT_TARGET) {
            if (isSupporter(selectedAttacker) && !isDealerMode(selectedAttacker)) {
                if (isEnemy) return;
                if (target == selectedAttacker) return;
                executeUserSkill(selectedAttacker, target);
            } else {
                if (!isEnemy) return;
                executeUserAttack(selectedAttacker, target);
            }
        }
    }

    // ㅡㅡㅡㅡㅡㅡ 전투 실행 ㅡㅡㅡㅡㅡㅡ

    private void executeUserAttack(Player attacker, Player target) {
        checkAndConvertIfNeeded(attacker, "pierre");
        performAttack(attacker, target, "[PIERRE]", "[JOJA]");
        view.updateAllTeams(ps);
        if (Main.checkDefeatTeam(ps)) { endGame("PIERRE 팀 승리!"); return; }
        isDanger(target, view, false, () -> enterComputerTurn());
    }

    // 무기 공격 실행 (일회용)
    private void executeUserWeaponAttack(Player attacker, Player target) {
        checkAndConvertIfNeeded(attacker, "pierre");

        attacker.attack(target, attacker.getWeapon());
        view.appendLog("[PIERRE] " + attacker.getName()
                + " → [JOJA] " + target.getName()
                + " 무기 공격! (남은 HP: " + Math.max(target.getHp(), 0) + ")", attacker.team);

        attacker.setWeapon(null); // 무기 소진 (일회용)

        view.updateAllTeams(ps);
        if (Main.checkDefeatTeam(ps)) { endGame("PIERRE 팀 승리!"); return; }
        isDanger(target, view, false, () -> enterComputerTurn());
    }

    private void executeComputerAttack() {
        List<Player> aliveAttackers = getAlivePlayers(RIGHT_PS_IDX);
        if (aliveAttackers.isEmpty()) return;

        Player attacker = aliveAttackers.get(random.nextInt(aliveAttackers.size()));
        checkAndConvertIfNeeded(attacker, "joja");

        Player target = null;

        if (isSupporter(attacker)) {
            if (!isDealerMode(attacker)) {
                attacker.useSkill(ps, null);
                view.appendLog("[JOJA] " + attacker.getName() + " 스킬 사용!", attacker.team);
            } else {
                List<Player> aliveTargets = getAlivePlayers(LEFT_PS_IDX);
                if (aliveTargets.isEmpty()) return;
                target = aliveTargets.get(random.nextInt(aliveTargets.size()));
                attacker.useSkill(ps, target);
                view.appendLog("[JOJA] " + attacker.getName()
                        + " → [PIERRE] " + target.getName()
                        + " 공격! (남은 HP: " + Math.max(target.getHp(), 0) + ")", attacker.team);
            }
        } else {
            List<Player> aliveTargets = getAlivePlayers(LEFT_PS_IDX);
            if (aliveTargets.isEmpty()) return;
            target = aliveTargets.get(random.nextInt(aliveTargets.size()));
            performAttack(attacker, target, "[JOJA]", "[PIERRE]");
        }

        view.updateAllTeams(ps);
        if (Main.checkDefeatTeam(ps)) { endGame("JOJA 팀 승리!"); return; }

        if (target != null) {
            final Player finalTarget = target;
            isDanger(finalTarget, view, true, () -> enterUserSelectAttacker());
        } else {
            enterUserSelectAttacker();
        }
    }

    private void performAttack(Player attacker, Player target,
                                String attackerTag, String targetTag) {
        if (attacker.getWeapon() != null) {
            attacker.attack(target, attacker.getWeapon());
        } else {
            attacker.attack(target);
        }
        view.appendLog(attackerTag + " " + attacker.getName()
                + " → " + targetTag + " " + target.getName()
                + " 공격! (남은 HP: " + Math.max(target.getHp(), 0) + ")", attacker.team);
    }

    private void executeUserSkill(Player attacker, Player target) {
        checkAndConvertIfNeeded(attacker, "pierre");
        attacker.useSkill(ps, target);
        view.appendLog("[PIERRE] " + attacker.getName()
                + " → [PIERRE] " + target.getName() + " 스킬 사용!", attacker.team);
        view.updateAllTeams(ps);
        if (Main.checkDefeatTeam(ps)) { endGame("PIERRE 팀 승리!"); return; }
        isDanger(target, view, true, () -> enterComputerTurn());
    }

    // ㅡㅡㅡㅡㅡㅡ 하비/거스 딜러 전환 ㅡㅡㅡㅡㅡㅡ

    private void checkAndConvertIfNeeded(Player attacker, String team) {
        if (!Player.hasNormalAttacker(ps, team)) {
            if (attacker instanceof 하비 && !((하비) attacker).isDamageDealer()) {
                ((하비) attacker).convertToDamageDealer();
                view.appendLog("⚔️ " + attacker.getName() + " 딜러로 전환!", "reset");
            } else if (attacker instanceof 거스 && !((거스) attacker).isDamageDealer()) {
                ((거스) attacker).convertToDamageDealer();
                view.appendLog("⚔️ " + attacker.getName() + " 딜러로 전환!", "reset");
            }
        }
    }

    // ㅡㅡㅡㅡㅡㅡ 게임 종료 ㅡㅡㅡㅡㅡㅡ

    private void endGame(String msg) {
        state = TurnState.GAME_OVER;
        view.clearAllHighlights();
        view.appendLog("\n" + msg + " 게임 종료.", "reset");
        
        String winnerTeam = msg.contains("PIERRE") ? "Pierre" : "Joja";
        view.showWinner(ps, winnerTeam);
    }

    // ㅡㅡㅡㅡㅡㅡ 유틸 ㅡㅡㅡㅡㅡㅡ
    private List<Player> getAlivePlayers(int[] indices) {
        List<Player> list = new ArrayList<>();
        for (int i : indices) if (ps[i].isAlive()) list.add(ps[i]);
        return list;
    }

    private boolean isRightTeam(int psIdx) {
        for (int i : RIGHT_PS_IDX) if (i == psIdx) return true;
        return false;
    }

    private boolean isSupporter(Player p) {
        return p instanceof 하비 || p instanceof 거스;
    }

    private boolean isDealerMode(Player p) {
        if (p instanceof 하비) return ((하비) p).isDamageDealer();
        if (p instanceof 거스) return ((거스) p).isDamageDealer();
        return true;
    }
}