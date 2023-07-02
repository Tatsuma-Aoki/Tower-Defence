package uectd.game.titleScene;

import java.awt.event.*;

import uectd.gameSystem.SceneController;
import uectd.gameSystem.SceneModel;
import uectd.gameSystem.SceneView;

public class TitleSceneController extends SceneController {

    public TitleSceneController(SceneModel sceneModel, SceneView view) {
        super(sceneModel, view);
        ((TitleSceneView) view).startButton.addActionListener(this);
        ((TitleSceneView) view).exitButton.addActionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ((TitleSceneView) view).startButton) {
            TitleSceneView.gameSound.stop();
            ((TitleSceneModel) model).gameStart();
        } else if (e.getSource() == ((TitleSceneView) view).exitButton) {
            TitleSceneView.gameSound.stop();
            ((TitleSceneModel) model).exit();
        }
    }
}
