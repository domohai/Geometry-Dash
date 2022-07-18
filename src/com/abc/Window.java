package com.abc;
import com.util.Const;
import com.util.Time;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

public class Window extends JFrame implements Runnable {
    private static Window window = null;
    private boolean is_running = false;
    public boolean in_editor = true;
    
    public MouseController mouse_listener;
    public KeyController key_listener;
    
    private Scene current_scene = null;
    private Image dbImage = null;
    private Graphics dbGraphics = null;

    public Window() {
        this.mouse_listener = new MouseController();
        this.key_listener = new KeyController();
        
        this.setSize(Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(Const.SCREEN_TITLE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        this.addMouseListener(mouse_listener);
        this.addMouseMotionListener(mouse_listener);
        this.addKeyListener(key_listener);
        
    }

    public void init() {
        is_running = true;
        change_scene(GameState.LEVEL_EDITOR);
    }

    public static Window getWindow() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }
    
    public Scene getCurrent_scene() {
        return current_scene;
    }
    
    public void change_scene(GameState new_game_state) {
        switch (new_game_state) {
            case LEVEL_EDITOR:
                in_editor = true;
                current_scene = new LevelEditorScene("LevelEditor");
                current_scene.init();
                break;
            case LEVEL_SCENE:
                in_editor = false;
                current_scene = new LevelScene("Level scene");
                current_scene.init();
                break;
            default:
                System.out.println("Not a valid scene");
                current_scene = null;
                break;
        }
    }

    public void update(double dt) {
        current_scene.update(dt);
        draw(getGraphics());
    }
    
    public void draw(Graphics g) {
        if (dbImage == null) {
            dbImage = createImage(getWidth(), getHeight());
            dbGraphics = dbImage.getGraphics();
        }
        render_offscreen(dbGraphics);
        g.drawImage(dbImage, 0, 0, getWidth(), getHeight(),null);
    }
    
    public void render_offscreen(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        current_scene.draw(g2D);
    }

    @Override
    public void run() {
        double last_frame_time = 0.0;
        try {
            while (is_running) {
                double time = Time.get_time();
                double deltatime = time - last_frame_time;
                last_frame_time = time;

                update(deltatime);

                Thread.sleep(18);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
