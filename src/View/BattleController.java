package View;

import Player.Player;
import Player.하비;
import Player.거스;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;

public class BattleController {

    // ㅡㅡㅡㅡㅡㅡ 턴 상태 ㅡㅡㅡㅡㅡㅡㅡ
    private enum TurnState {
        USER_SELECT_ATTACKER,  // 유저가 공격자 선택 중
        USER_SELECT_TARGET,    // 유저가 타겟 선택 중
        COMPUTER_TURN,         // 컴퓨터 공격중
        GAME_OVER
    }

    private TurnState state = TurnState.USER_SELECT_ATTACKER;

    // ㅡㅡㅡㅡㅡㅡ 필드 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    private final Player[]    ps;
    private final BattleView  view;
    private final Random      random = new Random();

    // ps 인덱스 매핑: 왼쪽(유저) -->짝수, 오른쪽(컴퓨터) --> 홀수
    private static final int[] LEFT_PS_IDX  = {0, 2, 4};
    private static final int[] RIGHT_PS_IDX = {1, 3, 5};

    private Player selectedAttacker = null; // 유저가 선택한 공격자

    // ,ㅡㅡㅡㅡㅡㅡㅡ 생성자 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    public BattleController(Player[] ps, BattleView view) {
        this.ps   = ps;
        this.view = view;
        registerButtons();
        enterUserSelectAttacker();
    }

    // ㅡㅡㅡㅡㅡㅡㅡ 버튼/클릭 콜백 등록 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    private void registerButtons() {
        // 왼쪽 공격버튼 3개
        for (int i = 0; i < 3; i++) {
            final int panelIdx = i;
            view.setOnAttackBtnClicked(i, () -> onAttackBtnClicked(panelIdx));
        }

        // 양쪽 모든 패널 클릭(타겟 선택용)
        for (int i = 0; i < 3; i++) {
            final int idx = i;
            view.setOnUnitClicked(true,  i, () -> onTargetClicked(LEFT_PS_IDX[idx]));
            view.setOnUnitClicked(false, i, () -> onTargetClicked(RIGHT_PS_IDX[idx]));
        }
    }

    // ── 상태 전환 메서드 ──────────────────────────────────

    private void enterUserSelectAttacker() {
        state = TurnState.USER_SELECT_ATTACKER;
        selectedAttacker = null;
        view.clearAllHighlights();
        view.appendLog("[USER] 공격할 캐릭터의 [공격] 버튼을 누르세요.");
    }

    private void enterUserSelectTarget(int panelIdx) {
        state = TurnState.USER_SELECT_TARGET;
        view.highlightAsAttacker(panelIdx);
        view.highlightAllEnemies(); // 오른쪽 패널 노란 테두리
        view.appendLog("[USER] 공격할 대상 캐릭터를 클릭하세요.");
    }

    private void enterComputerTurn() {
        state = TurnState.COMPUTER_TURN;
        view.clearAllHighlights();
        view.appendLog("[COMPUTER] 자동 공격 중...");

        // 1초 딜레이 후 자동 공격
        Timer timer = new Timer(1000, e -> {
            ((Timer) e.getSource()).stop();
            if (state == TurnState.COMPUTER_TURN) {
                executeComputerAttack();
            }
        });
        timer.start();
    }

    // ㅡㅡㅡㅡㅡ 이벤트 핸들러 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    // 왼쪽 공격버튼 클릭
    private void onAttackBtnClicked(int panelIdx) {
        if (state != TurnState.USER_SELECT_ATTACKER) return;

        Player attacker = ps[LEFT_PS_IDX[panelIdx]];
        if (!attacker.isAlive()) return;

        selectedAttacker = attacker;

        // 힐러/서포터 모드면 아군 선택 단계로
        if (isSupporter(attacker) && !isDealerMode(attacker)) {
            enterUserSelectAlly(panelIdx);
        } else {
            enterUserSelectTarget(panelIdx);
        }
    }

    // 캐릭터 클릭 (target 선택)
    private void onTargetClicked(int psIdx) {
        if (state != TurnState.USER_SELECT_TARGET) return;

        Player target = ps[psIdx];
        if (!target.isAlive()) return;

        boolean isEnemy = isRightTeam(psIdx);

        // 힐러/서포터 모드 --> 아군만 선택 가능
        if (isSupporter(selectedAttacker) && !isDealerMode(selectedAttacker)) {
            if (isEnemy) return; // 적군 클릭 무시
            if (target == selectedAttacker) return; // 자기 자신 무시
            executeUserSkill(selectedAttacker, target);
        } else {
            if (!isEnemy) return; // 아군 클릭 무시
            executeUserAttack(selectedAttacker, target);
        }
    }

    // ㅡㅡㅡㅡㅡㅡㅡ 전투 실행 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    private void executeUserAttack(Player attacker, Player target) {
        // 하비/거스 딜러 전환 체크 (아군에 일반 공격자 없으면 전환)
        checkAndConvertIfNeeded(attacker, "red");

        performAttack(attacker, target, "[RED]", "[BLUE]");

        view.updateAllTeams(ps);

        if (Main.checkDefeatTeam(ps)) {
            endGame("RED 팀 승리!");
            return;
        }

        enterComputerTurn();
    }

    private void executeComputerAttack() {
        List<Player> aliveAttackers = getAlivePlayers(RIGHT_PS_IDX);
        if (aliveAttackers.isEmpty()) return;

        Player attacker = aliveAttackers.get(random.nextInt(aliveAttackers.size()));
        checkAndConvertIfNeeded(attacker, "blue");

        if (isSupporter(attacker)) {
            if (!isDealerMode(attacker)) {
                // 힐러/서포터 모드 → 아군 대상 (useSkill 내부에서 처리)
                attacker.useSkill(ps, null);
                view.appendLog("[BLUE] " + attacker.getName() + " 스킬 사용!");
            } else {
                // 딜러 모드 → 적군(왼쪽팀) 랜덤 타겟 지정
                List<Player> aliveTargets = getAlivePlayers(LEFT_PS_IDX);
                if (aliveTargets.isEmpty()) return;
                Player target = aliveTargets.get(random.nextInt(aliveTargets.size()));
                attacker.useSkill(ps, target); // target = 적군
                view.appendLog("[BLUE] " + attacker.getName()
                        + " → [RED] " + target.getName()
                        + " 공격! (남은 HP: " + Math.max(target.getHp(), 0) + ")");
            }
        } else {
            List<Player> aliveTargets = getAlivePlayers(LEFT_PS_IDX);
            if (aliveTargets.isEmpty()) return;
            Player target = aliveTargets.get(random.nextInt(aliveTargets.size()));
            performAttack(attacker, target, "[BLUE]", "[RED]");
        }

        view.updateAllTeams(ps);

        if (Main.checkDefeatTeam(ps)) {
            endGame("BLUE 팀 승리!");
            return;
        }

        enterUserSelectAttacker();
    }

    // 실제 공격 처리 (무기 유무 분기)
    private void performAttack(Player attacker, Player target,
                                String attackerTag, String targetTag) {
        if (attacker.getWeapon() != null) {
            attacker.attack(target, attacker.getWeapon());
        } else {
            attacker.attack(target);
        }
        view.appendLog(attackerTag + " " + attacker.getName()
                + " → " + targetTag + " " + target.getName()
                + " 공격! (남은 HP: " + Math.max(target.getHp(), 0) + ")");
    }
    
    private void enterUserSelectAlly(int panelIdx) {
        state = TurnState.USER_SELECT_TARGET;
        view.highlightAsAttacker(panelIdx);
        view.highlightAllAllies(panelIdx); // 자신 제외 왼쪽 패널 강조
        view.appendLog("[USER] 스킬을 사용할 아군 캐릭터를 클릭하세요.");
    }
    
    private void executeUserSkill(Player attacker, Player target) {
        checkAndConvertIfNeeded(attacker, "red");

        attacker.useSkill(ps, target);
        view.appendLog("[RED] " + attacker.getName()
                + " → [RED] " + target.getName() + " 스킬 사용!");

        view.updateAllTeams(ps);

        if (Main.checkDefeatTeam(ps)) {
            endGame("RED 팀 승리!");
            return;
        }

        enterComputerTurn();
    }

    // ── 하비/거스 딜러 전환 체크 ──────────────────────────

    /**
     * 해당 팀에 일반 공격자(하비·거스가 아닌 생존자)가 없으면
     * 하비·거스를 딜러 모드로 전환한다.
     */
    private void checkAndConvertIfNeeded(Player attacker, String team) {
        if (!Player.hasNormalAttacker(ps, team)) {
            if (attacker instanceof 하비 && !((하비) attacker).isDamageDealer()) {
                ((하비) attacker).convertToDamageDealer();
                view.appendLog("⚔️ " + attacker.getName() + " 딜러로 전환!");
            } else if (attacker instanceof 거스 && !((거스) attacker).isDamageDealer()) {
                ((거스) attacker).convertToDamageDealer();
                view.appendLog("⚔️ " + attacker.getName() + " 딜러로 전환!");
            }
        }
    }

    // ㅡㅡㅡㅡㅡ 게임 종료 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    private void endGame(String msg) {
        state = TurnState.GAME_OVER;
        view.clearAllHighlights();
        view.appendLog("\n" + msg + " 게임 종료.");
    }

    // ㅡㅡㅡㅡㅡㅡㅡ 유틸 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    private List<Player> getAlivePlayers(int[] indices) {
        List<Player> list = new ArrayList<>();
        for (int i : indices) {
            if (ps[i].isAlive()) list.add(ps[i]);
        }
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