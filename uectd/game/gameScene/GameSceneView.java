
package uectd.game.gameScene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uectd.game.ResourcePathDefines;
import uectd.game.gameScene.gameMain.Tower;
import uectd.game.gameScene.GameSceneModel.GameState;
import uectd.game.gameScene.gameMain.BaseTurret;
import uectd.gameSystem.Define;
import uectd.gameSystem.FatalError;
import uectd.gameSystem.SceneModel;
import uectd.gameSystem.SceneView;
import uectd.gameSystem.util.ImageManager;
import uectd.gameSystem.util.ObservableComponent;
import uectd.gameSystem.util.SoundManager;

public class GameSceneView extends SceneView implements Observer {
    private Font font; // UI表示に用いるフォント

    private InformationPanel informationPanel; // 情報表示のためのパネル。
    public UpgradeTurretPanel upgradeTurretPanel; // 強化対象のタレットとその情報、タレット強化のためのボタンを配置したパネル
    public AddTurretPanel addTurretPanel; // 配置するタレットを選択するためのボタンを配置したパネル
    private GameSceneModel gSceneModel; // モデルのインスタンス

    private ObservableComponent<GameState> currentState; // モデルの現在の状態

    public GameSceneView(SceneModel model) {
        super(model);
        this.setBackground(Color.WHITE);
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(ResourcePathDefines.FONT_PATH));
        } catch (FontFormatException e) {
            e.printStackTrace();
            FatalError.quit("フォント形式エラー");
        } catch (IOException e) {
            e.printStackTrace();
            FatalError.quit("フォント読み込みエラー");
        }
        font = font.deriveFont(40f);

        gSceneModel = (GameSceneModel) model;
        informationPanel = new InformationPanel();
        upgradeTurretPanel = new UpgradeTurretPanel();
        addTurretPanel = new AddTurretPanel();

        this.setLayout(null);

        informationPanel.setBounds(0, 0, 700, 128);
        addTurretPanel.setBounds(0, Define.WINDOW_HEIGHT - 110 - 37, 700, 110);
        upgradeTurretPanel.setBounds(700, 0, Define.WINDOW_WIDTH - 700 - 15, Define.WINDOW_HEIGHT - 37);

        this.add(informationPanel);
        this.add(upgradeTurretPanel);
        this.add(addTurretPanel);

        currentState = gSceneModel.currentState;
        currentState.addObserver(this);
    }

    private class InformationPanel extends JPanel implements Observer {
        private static final String COUNTDOWN_STRING = "", WAVE_NUM_STRING = "W", BALANCE_STRING = "所持金：",
                CURRENCY_UNIT_STRING = "万円", TARGET_TOWERS_STRING = "防衛対象：";
        private JLabel countDownLabel, waveNumLabel, targetTowersLabel, balanceLabel;
        private ObservableComponent<Integer> countdownNum, waveNum, balance;
        private ObservableComponent<ArrayList<Tower>> targetTowers;
        private Image backgroundImage;

        public InformationPanel() {
            super();
            backgroundImage = ImageManager.getInstance().getImage(ResourcePathDefines.FRAME_IMAGE_PATH);
            this.setOpaque(false);

            countdownNum = gSceneModel.countdownNum;
            countdownNum.addObserver(this);
            waveNum = gSceneModel.waveNum;
            waveNum.addObserver(this);
            balance = gSceneModel.balance;
            balance.addObserver(this);

            countDownLabel = new JLabel(COUNTDOWN_STRING + countdownNum.getValue());
            countDownLabel.setForeground(Color.WHITE);
            countDownLabel.setFont(font);
            waveNumLabel = new JLabel(WAVE_NUM_STRING + waveNum.getValue());
            waveNumLabel.setForeground(Color.WHITE);
            waveNumLabel.setFont(font);
            targetTowersLabel = new JLabel(makeTargetTowersLabelString());
            targetTowersLabel.setForeground(Color.WHITE);
            targetTowersLabel.setFont(font);
            balanceLabel = new JLabel(BALANCE_STRING + balance.getValue() + CURRENCY_UNIT_STRING);
            balanceLabel.setForeground(Color.WHITE);
            balanceLabel.setFont(font);

            GridBagLayout layout = new GridBagLayout();
            this.setLayout(layout);

            GridBagConstraints gbc = new GridBagConstraints();

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.weightx = 1d;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(15, 30, 15, 10);
            layout.setConstraints(waveNumLabel, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.weightx = 1d;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(15, 30, 15, 10);
            layout.setConstraints(countDownLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.gridheight = 1;
            gbc.weightx = 1d;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(15, 10, 15, 10);
            layout.setConstraints(targetTowersLabel, gbc);
            targetTowers = gSceneModel.targets;
            targetTowersLabel.setText(makeTargetTowersLabelString());

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.weightx = 1d;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(15, 10, 15, 10);
            layout.setConstraints(balanceLabel, gbc);

            this.add(countDownLabel);
            this.add(waveNumLabel);
            this.add(balanceLabel);
            this.add(targetTowersLabel);
        }

        private String makeTargetTowersLabelString() {
            String res = "";
            if (targetTowers != null) {
                var targetsList = targetTowers.getValue();
                for (int i = 0; i < targetsList.size(); i++) {
                    if (i != 0)
                        res += ", ";
                    res += targetsList.get(i).getName();
                }
            }
            return TARGET_TOWERS_STRING + res;
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
            super.paintComponent(g);
        }

        @Override
        public void update(Observable o, Object arg) {
            if (o == countdownNum) {
                countDownLabel.setText(COUNTDOWN_STRING + countdownNum.getValue());
            } else if (o == waveNum) {
                waveNumLabel.setText(WAVE_NUM_STRING + waveNum.getValue());
            } else if (o == targetTowers) {
                targetTowersLabel.setText(makeTargetTowersLabelString());
            } else if (o == balance) {
                balanceLabel.setText(BALANCE_STRING + balance.getValue());
            }
        }
    }

    public class UpgradeTurretPanel extends JPanel implements Observer {
        private ObservableComponent<BaseTurret> selectingTurret;
        private Image backgroundImage;

        private TurretImage turretImage;
        public JButton rangeButton, powerButton, rofButton, sellButton;
        private JLabel turretNameLabel;
        private Font upgradePanelFont;

        private class TurretImage extends JPanel {
            private Image image, frameImage;

            public TurretImage() {
                this.setOpaque(false);
                this.image = null;
                this.frameImage = ImageManager.getInstance().getImage(ResourcePathDefines.TURRET_FRAME_IMAGE_PATH);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (image != null) {
                    int width = 0, height = 0;
                    if (this.getWidth() > this.getHeight()) {
                        height = this.getHeight();
                        width = (int) (image.getWidth(null) * ((float) height / image.getHeight(null)));
                    } else {
                        width = this.getWidth();
                        height = (int) (image.getHeight(null) * ((float) width / image.getWidth(null)));
                    }
                    g.drawImage(frameImage, this.getWidth() / 2 - width / 2, this.getHeight() / 2 - height / 2, width,
                            height, null);
                    g.drawImage(image, this.getWidth() / 2 - width / 2, this.getHeight() / 2 - height / 2, width,
                            height, null);
                }
            }
        }

        public UpgradeTurretPanel() {
            super();
            this.setOpaque(false);

            backgroundImage = ImageManager.getInstance().getImage(ResourcePathDefines.UPGRADE_FRAME_IMAGE_PATH);
            selectingTurret = gSceneModel.selectingTurret;
            selectingTurret.addObserver(this);
            this.upgradePanelFont = font.deriveFont(18);

            turretImage = new TurretImage();
            rangeButton = new JButton("範");
            rangeButton.setFont(upgradePanelFont);
            powerButton = new JButton("威");
            powerButton.setFont(upgradePanelFont);
            rofButton = new JButton("速");
            rofButton.setFont(upgradePanelFont);
            sellButton = new JButton("売");
            sellButton.setFont(upgradePanelFont);
            turretNameLabel = new JLabel("");
            turretNameLabel.setHorizontalAlignment(JLabel.CENTER);
            turretNameLabel.setFont(font.deriveFont(20));
            turretNameLabel.setForeground(Color.WHITE);

            GridBagLayout layout = new GridBagLayout();
            this.setLayout(layout);

            GridBagConstraints gbc = new GridBagConstraints();

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.gridheight = 2;
            gbc.weightx = 1d;
            gbc.weighty = 1d;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(25, 35, 25, 35);
            layout.setConstraints(turretImage, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.gridheight = 1;
            gbc.weightx = 1d;
            gbc.weighty = 1d;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(25, 25, 25, 25);
            layout.setConstraints(turretNameLabel, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            gbc.gridheight = 1;
            gbc.weightx = 1d;
            gbc.weighty = 1d;
            gbc.insets = new Insets(5, 15, 5, 15);
            layout.setConstraints(rangeButton, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            gbc.gridheight = 1;
            gbc.weightx = 1d;
            gbc.weighty = 1d;
            gbc.insets = new Insets(5, 15, 5, 15);
            layout.setConstraints(powerButton, gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 2;
            gbc.gridheight = 1;
            gbc.weightx = 1d;
            gbc.weighty = 1d;
            gbc.insets = new Insets(5, 15, 5, 15);
            layout.setConstraints(rofButton, gbc);

            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.gridwidth = 2;
            gbc.gridheight = 1;
            gbc.weightx = 1d;
            gbc.weighty = 1d;
            gbc.insets = new Insets(5, 15, 5, 15);
            layout.setConstraints(sellButton, gbc);

            this.add(turretImage);
            this.add(turretNameLabel);
            this.add(rangeButton);
            this.add(powerButton);
            this.add(rofButton);
            this.add(sellButton);
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
            super.paintComponent(g);
        }

        @Override
        public void update(Observable o, Object arg) {
            if (o == selectingTurret) {
                BaseTurret turret = selectingTurret.getValue();
                if (turret != null) {
                    turretImage.image = selectingTurret.getValue().getImage();
                    turretNameLabel.setText(selectingTurret.getValue().getName());
                    rangeButton.setText("範:Lv" + selectingTurret.getValue().getRangeLevel() + ":"
                            + selectingTurret.getValue().getTurretCollisionRadiusLevelupPrice());
                    powerButton.setText("威:Lv" + selectingTurret.getValue().getAttackLevel() + ":"
                            + selectingTurret.getValue().getAttackPowerLevelupPrice());
                    rofButton.setText("速:Lv" + selectingTurret.getValue().getRofLevel() + ":"
                            + selectingTurret.getValue().getRofLevelupPrice());
                } else {
                    turretImage.image = null;
                    turretNameLabel.setText("");
                    rangeButton.setText("範");
                    powerButton.setText("威");
                    rofButton.setText("速");
                }
            }
        }
    }

    public class AddTurretPanel extends JPanel implements Observer {
        private Font priceFont;

        public class TurretDisplayButton extends JButton {
            Image image, frameImage;
            int price;
            JLabel priceLabel;
            BaseTurret turret;
            boolean isSelecting;

            public TurretDisplayButton(BaseTurret turret) {
                super(new ImageIcon(turret.getImage()));
                this.turret = turret;
                this.setContentAreaFilled(false);
                this.image = turret.getImage();
                this.price = turret.getPurchasePrice();
                this.priceLabel = new JLabel("" + this.price);
                this.priceLabel.setFont(priceFont);
                this.priceLabel.setForeground(Color.BLACK);
                this.priceLabel.setBackground(new Color(200, 200, 200, 100));
                this.priceLabel.setAlignmentY(100);
                this.priceLabel.setAlignmentX(0);
                this.frameImage = ImageManager.getInstance().getImage(ResourcePathDefines.TURRET_BUTTON_IMAGE_PATH);
                add(this.priceLabel);
            }

            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(frameImage, 0, 0, this.getWidth(), this.getHeight(), null);
                if (image != null) {
                    int width = 0, height = 0;
                    if (isSelecting) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setColor(Color.GRAY);
                        g2.setStroke(new BasicStroke(13));
                        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
                    }
                    if (this.getWidth() > this.getHeight()) {
                        height = this.getHeight();
                        width = (int) (image.getWidth(null) * ((float) height / image.getHeight(null)));
                    } else {
                        width = this.getWidth();
                        height = (int) (image.getHeight(null) * ((float) width / image.getWidth(null)));
                    }
                    g.drawImage(image, this.getWidth() / 2 - width / 2, this.getHeight() / 2 - height / 2, width,
                            height, null);
                }
            }
        }

        private Image backgroundImage;

        private ObservableComponent<ArrayList<BaseTurret>> availableTurrets;
        public ObservableComponent<ArrayList<TurretDisplayButton>> turretButtons;
        public ObservableComponent<BaseTurret> purchasingTurret;

        public AddTurretPanel() {
            this.setOpaque(false);
            priceFont = font.deriveFont(20.0f);
            backgroundImage = ImageManager.getInstance().getImage(ResourcePathDefines.ADD_TURRET_FRAME_IMAGE_PATH);
            availableTurrets = gSceneModel.availableTurretList;
            availableTurrets.addObserver(this);
            turretButtons = new ObservableComponent<>(new ArrayList<TurretDisplayButton>());
            purchasingTurret = gSceneModel.purchasingTurret;
            purchasingTurret.addObserver(this);
            makeTurretButtons();
        }

        private void makeTurretButtons() {
            for (var button : turretButtons.getValue()) {
                this.remove(button);
            }

            var buttonList = new ArrayList<TurretDisplayButton>();
            for (var turret : availableTurrets.getValue()) {
                var turretDisplayButton = new TurretDisplayButton(turret);
                buttonList.add(turretDisplayButton);
                turretDisplayButton.setPreferredSize(new Dimension(100, 100));
                this.add(turretDisplayButton);
            }

            turretButtons.setValue(buttonList);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
        }

        @Override
        public void update(Observable o, Object arg) {
            if (o == availableTurrets) {
                makeTurretButtons();
            } else if (o == purchasingTurret) {
                for (var turretButton : turretButtons.getValue()) {
                    turretButton.isSelecting = (turretButton.turret == purchasingTurret.getValue());
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == currentState) {
            if (currentState.getValue() == GameState.OnWave) {
                switch (gSceneModel.waveNum.getValue()) {
                    case 1:
                        SoundManager.getInstance().play(ResourcePathDefines.WAVE1_SOUND_PATH);
                        break;
                    case 4:
                        SoundManager.getInstance().play(ResourcePathDefines.WAVE3_SOUND_PATH);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
