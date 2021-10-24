package view;

import map.*;
import data.*;
import elements.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MapFrame extends JPanel implements ActionListener, Runnable, MouseListener {
    private MapPanel mapPanel;
    private int widthMapPanel;
    private int heightMapPanel;
    private StatisticPanel statisticPanel;
    private JButton buttonPause;
    private JButton buttonGenotype;
    private JButton buttonStatistic;
    private Engine engine;
    private int ratio;
    private int timeStamp;
    private boolean isStart;
    private boolean flag = true;


    public MapFrame (int width, int height, double jungleRatio, int startEnergy, int moveEnergy, int plantEnergy, int numberOfAnimals, int numberOfPlants, int timeStamp) {
        this.setBackground(new Color(236, 207, 172));
        this.timeStamp=timeStamp;
        ratio =(int) (Math.min (1000/height, 500/width));
        engine = new Engine(width, height, jungleRatio, startEnergy, moveEnergy, plantEnergy, numberOfAnimals, numberOfPlants);
        addMouseListener(this);

        widthMapPanel=ratio*engine.getMap().getArea().getWidth();
        heightMapPanel=ratio*engine.getMap().getArea().getHeight();
        mapPanel = new MapPanel(ratio);
        mapPanel.setBounds(370, 0, widthMapPanel, heightMapPanel);
        this.add(mapPanel);
        statisticPanel= new StatisticPanel();
        statisticPanel.setBounds(0,45,350,700);
        this.add(statisticPanel);

        buttonPause = new JButton();
        buttonPause.setBounds(10,10,150,25);
        buttonPause.addActionListener(this);
        buttonPause.setText("Start / Stop");
        this.add(buttonPause);

        buttonGenotype = new JButton();
        buttonGenotype.setBounds(10,860,300,30);
        buttonGenotype.addActionListener(this);
        buttonGenotype.setText("Dominujący genotyp");
        this.add(buttonGenotype);

        buttonStatistic = new JButton();
        buttonStatistic.setBounds(10,900,300,30);
        buttonStatistic.addActionListener(this);
        buttonStatistic.setText("Zapisz statystyki do pliku");
        this.add(buttonStatistic);

        writeKey();

        isStart=false;
    }

    private void writeKey () {
        JPanel key = new JPanel();
        key.setBackground(new Color(248, 5, 5, 37));
        key.setBounds(5,750,200,100);
        JPanel grassFieldColor = new JPanel();
        grassFieldColor.setBackground(new Color(57, 245, 83));
        grassFieldColor.setBounds(5,5,15,15);
        JLabel grassFieldName = new JLabel("Step");
        grassFieldName.setBounds(25,5,100,15);
        key.add(grassFieldName);
        key.add(grassFieldColor);

        JPanel jungleColor = new JPanel();
        jungleColor.setBackground(new Color(16, 92, 29));
        jungleColor.setBounds(5,30,15,15);
        JLabel jungleName = new JLabel("Dżungla");
        jungleName.setBounds(25,30,100,15);
        key.add(jungleColor);
        key.add(jungleName);

        JPanel plantColor = new JPanel();
        plantColor.setBackground(new Color(16, 182, 38));
        plantColor.setBounds(5,55,15,15);
        JLabel plantName = new JLabel("Rośliny");
        plantName.setBounds(25,55,100,15);
        key.add(plantColor);
        key.add(plantName);

        JPanel animalColor = new JPanel();
        animalColor.setBackground(new Color(59, 94, 220));
        animalColor.setBounds(5,80,15,15);
        JLabel animalName = new JLabel("Zwierzęta");
        animalName.setBounds(25,80,100,15);
        key.add(animalColor);
        key.add(animalName);

        this.add(key);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==buttonPause && !isStart) {
            isStart=true;
            new Thread(this).start();
        }
        else if(e.getSource()==buttonPause && isStart) {
            isStart=false;
        }
        if(e.getSource()==buttonGenotype && !isStart) {
            mapPanel.markDominantGenotype(engine.getStatus().getDominantGenes(), engine.getMap());
        }
        if(e.getSource()==buttonStatistic && !isStart) {
            new CreateStatistic(engine);
        }
    }

    @Override
    public void run() {
        while (isStart) {
            flag = true;
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    engine.simulate();
                    mapPanel.drawMap(engine.getStatus(), engine.getMap());
                    statisticPanel.updateCharts(engine.getStatus(), engine.getDay());
                    flag=false;
                }
            });
            if (engine.getStatus().getNumberOfAnimals() == 0)
                break;
            try {
                Thread.sleep(timeStamp);
            } catch (InterruptedException exc) {
            }
            while (flag) { //zwielokrotnienie time stamp aby mapa zdązył się zaaktualizować
                try {
                    Thread.sleep(timeStamp);
                } catch (InterruptedException exc) {
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (isStart) return;
        int x = e.getX();
        int y = e.getY();
        if (x<370 || x>370+widthMapPanel || y>heightMapPanel) return;
        x=x-370;
        x=x/ratio;
        y=y/ratio;
        if(engine.getStatus().getElements().get(new Vector2d(x,y)) instanceof Animal) {
            new AnimalTracking(engine, new Vector2d(x,y));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}

