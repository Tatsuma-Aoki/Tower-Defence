package uectd.game.gameScene;

import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;

import uectd.game.gameScene.GameSceneView.AddTurretPanel.TurretDisplayButton;
import uectd.gameSystem.SceneController;
import uectd.gameSystem.SceneModel;
import uectd.gameSystem.SceneView;
import uectd.gameSystem.util.IClickable;
import uectd.gameSystem.util.ObservableComponent;
import uectd.gameSystem.util.Vector2;

public class GameSceneController extends SceneController implements Observer {
    private boolean isWKeyPressed;
    private boolean isAKeyPressed;
    private boolean isSKeyPressed;
    private boolean isDKeyPressed;
    private boolean isSHIFTKeyPressed;
    private boolean isSPACEKeyPressed;
    private GameSceneModel gameModel;
    private GameSceneView gameView;
    private GameSceneView.AddTurretPanel addTurretPanel;
    private GameSceneView.UpgradeTurretPanel upgradeTurretPanel;
    private JButton rangeButton, powerButton, rofButton, sellButton;
    private ObservableComponent<ArrayList<TurretDisplayButton>> turretButtons;

    public GameSceneController(SceneModel model, SceneView view) {
        super(model, view);
        gameModel = (GameSceneModel) model;
        gameView = (GameSceneView) view;
        addTurretPanel = gameView.addTurretPanel;
        turretButtons = gameView.addTurretPanel.turretButtons;
        turretButtons.addObserver(this);
        addActionListenerToTurretButtons();
        upgradeTurretPanel = gameView.upgradeTurretPanel;
        rangeButton = upgradeTurretPanel.rangeButton;
        powerButton = upgradeTurretPanel.powerButton;
        rofButton = upgradeTurretPanel.rofButton;
        sellButton = upgradeTurretPanel.sellButton;
        rangeButton.addActionListener(this);
        powerButton.addActionListener(this);
        rofButton.addActionListener(this);
        sellButton.addActionListener(this);
    }

    public void addActionListenerToTurretButtons() {
        for (TurretDisplayButton turretDisplayButton : turretButtons.getValue()) {
            turretDisplayButton.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        var src = e.getSource();
        if (src == rangeButton) {
            gameModel.upTurretRangeLevel();
        } else if (src == powerButton) {
            gameModel.upTurretAttackLevel();
        } else if (src == rofButton) {
            gameModel.upTurretRofLevel();
        } else if (src == sellButton) {
            gameModel.sellTurret();
        }
        for (TurretDisplayButton button : turretButtons.getValue()) {
            if (button == src) {
                gameModel.setPurchasingTurret(button.turret);
            }
        }

    }

    public void moveCamera() {
        Vector2 cameraVector = new Vector2();
        cameraVector.x = (isAKeyPressed ? -1 : 0) + (isDKeyPressed ? 1 : 0);
        cameraVector.y = (isSKeyPressed ? 1 : 0) + (isWKeyPressed ? -1 : 0);
        gameModel.setCameraMoveDirection(cameraVector.normalized());
    }

    public void updateScaleOperation() {
        int scale = 0;
        scale += (isSHIFTKeyPressed ? 1 : 0);
        scale += (isSPACEKeyPressed ? -1 : 0);
        // scale = 1 --> 拡大; scale = 0 -->何もしない; scale = -1 --> 縮小;
        gameModel.setCameraZoomOperation(scale);// modelにscaleを渡す。
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // クリックした座標を受け取ったら、その座補をワールド座標に変換してから、クリック可能なゲームオブジェクトをクリックしているかどうかを確かめて、もしクリックしていたらmodelにそれを通知。クリックしていなかったらNULLを返す。
        // クリックしているかどうかの処理はfindClickedObject
        // 例はClickSampleのGameSecenControllerのmousePressed
        Point point = e.getPoint();
        Vector2 worldPos = view.convertScreenPosToWorldPos(new Vector2(point.x, point.y));
        IClickable clicked = findClickedObject(worldPos);
        gameModel.receiveIClickable(clicked);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_ESCAPE) {
            ((GameSceneModel) model).pauseKeyPressed();
        }

        if (keyCode == KeyEvent.VK_W) {
            isWKeyPressed = true;
        }
        if (keyCode == KeyEvent.VK_A) {
            isAKeyPressed = true;
        }
        if (keyCode == KeyEvent.VK_S) {
            isSKeyPressed = true;
        }
        if (keyCode == KeyEvent.VK_D) {
            isDKeyPressed = true;
        }

        if (keyCode == KeyEvent.VK_SHIFT) {
            isSHIFTKeyPressed = true;
        }
        if (keyCode == KeyEvent.VK_SPACE) {
            isSPACEKeyPressed = true;
        }

        if (isWKeyPressed || isAKeyPressed || isSKeyPressed || isDKeyPressed) {
            moveCamera();
        }

        if (isSHIFTKeyPressed || isSPACEKeyPressed) {
            updateScaleOperation();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_W) {
            isWKeyPressed = false;
        }
        if (keyCode == KeyEvent.VK_A) {
            isAKeyPressed = false;
        }
        if (keyCode == KeyEvent.VK_S) {
            isSKeyPressed = false;
        }
        if (keyCode == KeyEvent.VK_D) {
            isDKeyPressed = false;
        }

        if (keyCode == KeyEvent.VK_SHIFT) {
            isSHIFTKeyPressed = false;
        }
        if (keyCode == KeyEvent.VK_SPACE) {
            isSPACEKeyPressed = false;
        }

        if (!(isWKeyPressed && isAKeyPressed && isSKeyPressed && isDKeyPressed)) {
            moveCamera();
        }

        if (!(isSHIFTKeyPressed && isSPACEKeyPressed)) {
            updateScaleOperation();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == turretButtons) {
            addActionListenerToTurretButtons();
        }
    }
}
