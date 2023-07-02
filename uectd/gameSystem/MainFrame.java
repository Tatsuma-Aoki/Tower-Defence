package uectd.gameSystem;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

import uectd.game.titleScene.*;
import uectd.gameSystem.util.Intent;

public class MainFrame extends JFrame implements SceneChangeListener {

    private JPanel mainPanel; // 基本的に全てのSwingの要素はこのパネルの下に置く
    private CardLayout layout; // カードレイアウトでシーンを切り替える

    private Stack<Scene> sceneStack; // シーンを保存しておくスタック

    public MainFrame() {
        sceneStack = new Stack<>();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createWindow();
            }
        });

    }

    public static void main(String argv[]) {
        new MainFrame();
    }

    private void createWindow() {
        this.mainPanel = new JPanel();
        this.layout = new CardLayout();
        this.mainPanel.setLayout(layout);

        this.add(mainPanel);

        this.setTitle(Define.WINDOW_TITLE);
        this.setSize(Define.WINDOW_WIDTH, Define.WINDOW_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);

        TitleScene scene = new TitleScene(this);
        sceneStack.push(scene);
        this.mainPanel.add(scene.view, scene.toString());
        scene.controller.start(null);

        this.setVisible(true);
    }

    @Override
    public void sceneChanged(Scene scene, boolean stackClearFlag, Intent intent) {
        sceneStack.peek().controller.stop();
        this.mainPanel.remove(sceneStack.peek().view);
        sceneStack.pop();
        if (stackClearFlag) {
            while (!sceneStack.empty()) {
                sceneStack.peek().controller.stop();
                this.mainPanel.remove(sceneStack.peek().view);
                sceneStack.pop();
            }
        }

        sceneStack.push(scene);
        this.mainPanel.add(scene.view, scene.view.toString());
        layout.addLayoutComponent(scene.view, null);
        layout.show(mainPanel, scene.view.toString());

        this.mainPanel.revalidate();
        scene.controller.start(intent);
    }

    @Override
    public void scenePopped(Intent intent) {
        sceneStack.peek().controller.stop();
        this.mainPanel.remove(sceneStack.peek().view);
        this.mainPanel.revalidate();
        sceneStack.pop();
        sceneStack.peek().controller.unpause(intent);
        layout.addLayoutComponent(sceneStack.peek().view, null);
        layout.show(mainPanel, sceneStack.peek().toString());
    }

    @Override
    public void scenePushed(Scene scene, Intent intent) {

        sceneStack.peek().controller.pause();
        sceneStack.push(scene);
        this.mainPanel.add(scene.view, scene.view.toString());
        layout.addLayoutComponent(scene.view, null);
        layout.show(mainPanel, scene.view.toString());
        layout.last(mainPanel);

        this.mainPanel.revalidate();
        scene.controller.start(intent);

    }

    @Override
    public void sceneChanged(Scene scene, Intent intent) {
        sceneChanged(scene, false, intent);
    }

}