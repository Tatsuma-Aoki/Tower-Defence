package uectd.game.pauseScene;

import java.awt.event.*;

import uectd.gameSystem.SceneController;
import uectd.gameSystem.SceneModel;
import uectd.gameSystem.SceneView;

public class PauseSceneController extends SceneController {

    public PauseSceneController(SceneModel sceneModel, SceneView view) {
        super(sceneModel, view);
        ((PauseSceneView) view).unpauseButton.addActionListener(this);
        ((PauseSceneView) view).exitButton.addActionListener(this);
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
        super.actionPerformed(e);
        if (e.getSource() == ((PauseSceneView) view).unpauseButton) {
            ((PauseSceneModel) model).unpause();
        } else if (e.getSource() == ((PauseSceneView) view).exitButton) {
            ((PauseSceneModel) model).exit();
        }
    }

}
