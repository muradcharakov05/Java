import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class  Fighter implements Attack,Deffence{

    int at =20;
    int zelat=0;
    int zeldef=0;
    int zelzam=0;
    int def=20;
    Scanner in=new Scanner(System.in);
    List<Integer> damages = new ArrayList<>();

    Head head = new Head();
    Body body = new Body();
    Legs legs = new Legs();


    class Head{
         int hhealth=100;
    }

     class Body{
        int bhealth=100;
    }
     class Legs{
        int lhealth=100;
    }

    @Override
    public void attackh(Fighter.Head h) {
        h.hhealth-=at;
    }

    @Override
    public void attackb(Fighter.Body b) {
    b.bhealth-=at;
    }

    @Override
    public void attackl(Fighter.Legs l) {
    l.lhealth-=at;
    }

    @Override
    public void hdeffence(Fighter.Head h) {
        h.hhealth+=def;

    }

    @Override
    public void bdeffence(Fighter.Body b) {
        b.bhealth+=def;
    }

    @Override
    public void ldeffence(Fighter.Legs l) {
    l.lhealth+=def;
    }
    }
