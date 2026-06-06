package View;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import Player.*;
import Weapon.*;
import javax.swing.JFrame;

public class Main {
    public static void shuffleArray(Player[] array) {
        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            Player temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    public static void selectTeam(Player[] ps) {
        for (int i = 0; i < ps.length; i++) {
            if (i % 2 == 0) ps[i].team = "pierre";
            else             ps[i].team = "joja";
        }
    }
    public static boolean checkDefeatTeam(Player[] ps) {
		boolean redDead = !ps[0].isAlive() && !ps[2].isAlive() && !ps[4].isAlive();
		boolean blueDead = !ps[1].isAlive() && !ps[3].isAlive() && !ps[5].isAlive();
		return redDead || blueDead;
	}


    public static void main(String[] args) {
        // Player 생성
        헤일리 Hailey = new 헤일리("헤일리", 200, 60);
        레아   Rea    = new 레아("레아",   200, 30);
        로빈   Robin  = new 로빈("로빈",   200, 40);
        마니   Mary   = new 마니("마니",   200, 50);
        거스   Gus    = new 거스("거스",   200, 10);
        하비   Harvey = new 하비("하비",   200, 100);

        // 무기 생성
        Rea.setWeapon(new Weapon("카메라", 40,  0.5, 1.2));
        Robin.setWeapon(new Weapon("도끼",  90,  0.2, 2.0));
        Mary.setWeapon(new Weapon("채찍",   100, 0.3, 1.5));

        Player[] ps = {Hailey, Rea, Mary, Gus, Harvey, Robin};

        shuffleArray(ps);
        selectTeam(ps);

        Random random = new Random();

        JFrame frame = new JFrame("3:3 스타듀 배틀");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 650);
        
        BattleView battleView = new BattleView();
        battleView.updateAllTeams(ps);
        new BattleController(ps, battleView);
        
        StartView startView = new StartView();
        startView.setOnPlayClicked(() -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(battleView);
            frame.revalidate();
            frame.repaint();
        });
        
   

        frame.add(startView);
        frame.setVisible(true);
    }
}