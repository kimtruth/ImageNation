import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Main extends JFrame {
    // 멤버변수
    Container pane = getContentPane();
    JPanel btnPane = new JPanel();
    JButton savebtn = new JButton("SAVE");
    JButton leftbtn = new JButton("<");
    JButton rightbtn = new JButton(">");
    int p_width = 400, p_height = 400; // window 창의 크기 (JFrame의 크기)

    // JMenu
    JMenuBar mb = new JMenuBar();
    JMenu[] menu = new JMenu[4];
    String menu_String[] = { "File","Insert", "Extract", "About Us" };
    String filemenu_String[] = { "Open", "Save", "Exit" };
    String insertmenu_String[] = {"Text"};
    JMenuItem JMenu_extractItem = new JMenuItem("Extract Text");
    JMenuItem JMenu_aboutItem = new JMenuItem("About Us");

    // insert_text dialog
    JDialog text_dialog = new JDialog(this, "텍스트 삽입", true);
    JTextArea insert_text = new JTextArea();
    JButton save_text_btn = new JButton("저장");
    JPanel save_btn_pane = new JPanel();


    // about_ this dialog
    JDialog about_dialog = new JDialog(this, "About Us", true);
    JLabel about_ta = new JLabel("    20608 김영호 20620 안우진");

    //ImageView
    JLabel imgLabel = new JLabel("이미지를 불러와 주세요", SwingConstants.CENTER);
    BufferedImage user_img = null;
    BufferedImage filtered_img = null;
    int filter_idx = 0;

    void msgbox(String s){
        JOptionPane.showMessageDialog(null, s);
    }

    // 생성자
    public Main() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(p_width, p_height); // frame 의 크기 설정
        setTitle("ImageNation");

        // GUI 화면 구성 시작
        jmenu_add();
        about_dialog.setSize(200,110);
        about_dialog.setLocation(200,200);
        about_dialog.add(about_ta);

        insert_text.setLineWrap(true);
        text_dialog.add(save_btn_pane, BorderLayout.SOUTH);
        text_dialog.setSize(250, 150);
        text_dialog.setLocation(200,200);
        text_dialog.add(insert_text);
        save_btn_pane.add(save_text_btn);
        save_text_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    user_img = MyFunction.insert_text_to_image(filtered_img, insert_text.getText());
                    msgbox("성공적으로 삽입되었습니다.");
                } catch (Exception e1){
                    msgbox("삽입하는데 문제가 발생하였습니다.");
                }
                text_dialog.dispose();
            }
        });

        pane.add(imgLabel, BorderLayout.CENTER);

        // 버튼 부분
        pane.add(btnPane, BorderLayout.SOUTH);
        btnPane.add(leftbtn);
        leftbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter_idx--;
                setImageFilter();
            }
        });
        rightbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter_idx++;
                setImageFilter();
            }
        });
        btnPane.add(savebtn);
        btnPane.add(rightbtn);

        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                setImageView();
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });

        savebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                img_save();
            }
        });

        // 화면 구성 종료
        setVisible(true); // 화면에 보여라. 맨 마지막에 적을 것.
    }

    private void setImageFilter() {
        if (filter_idx < 0)
            filter_idx = 5;
        else if (filter_idx > 5)
            filter_idx = 0;

        switch (filter_idx){
            case 0:
                filtered_img = user_img;
                break;
            case 1:
                filtered_img = new filter.Invert().filter(user_img);
                break;
            case 2:
                filtered_img = new filter.Blur().filter(user_img);
                break;
            case 3:
                filtered_img = new filter.Sharpen().filter(user_img);
                break;
            case 4:
                filtered_img = new filter.Old().filter(user_img);
                break;
            case 5:
                filtered_img = new filter.Sepia().filter(user_img);
                break;
        }
        System.out.println(filter_idx);
        setImageView();
    }

    public void setImageView() {
        if (filtered_img != null) {
            if (filtered_img.getWidth() > filtered_img.getHeight())
                imgLabel.setIcon(new ImageIcon(filtered_img.getScaledInstance(imgLabel.getWidth(), (int) (filtered_img.getHeight() / (double) filtered_img.getWidth() * imgLabel.getWidth()), Image.SCALE_DEFAULT)));
            else
                imgLabel.setIcon(new ImageIcon(filtered_img.getScaledInstance((int) (filtered_img.getWidth() / (double) filtered_img.getHeight() * imgLabel.getHeight()), imgLabel.getHeight(), Image.SCALE_DEFAULT)));
        }
    }

    public void jmenu_add() {

        JMenuItem[] JFMItem = new JMenuItem[filemenu_String.length];
        JMenuItem[] JIMItem = new JMenuItem[insertmenu_String.length];

        for (int i = 0; i < menu.length; i++) {
            menu[i] = new JMenu(menu_String[i]);
            mb.add(menu[i]);
        }
        for (int i = 0; i < JFMItem.length; i++) {
            JFMItem[i] = new JMenuItem(filemenu_String[i]);
            menu[0].add(JFMItem[i]);
            JFMItem[i].addActionListener(new MenuActionListener());
        }

        for (int i = 0; i < JIMItem.length; i++) {
            JIMItem[i] = new JMenuItem(insertmenu_String[i]);
            menu[1].add(JIMItem[i]);
            JIMItem[i].addActionListener(new MenuActionListener());
        }

        JMenu_extractItem.addActionListener(new MenuActionListener());
        menu[2].add(JMenu_extractItem);

        JMenu_aboutItem.addActionListener(new MenuActionListener());
        menu[3].add(JMenu_aboutItem);

        this.setJMenuBar(mb);
    }
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    public void img_save(){
        //파일 저장 코드
        JFileChooser file_chooser = new JFileChooser();// 객체 생성

        int users_value = file_chooser.showSaveDialog(file_chooser);

        if (users_value == JFileChooser.APPROVE_OPTION) {
            File picture = file_chooser.getSelectedFile();
            try {
                ImageIO.write(filtered_img, getExtension(picture), picture);
                msgbox("성공적으로 저장되었습니다.");
            } catch (IOException e) {
                msgbox("파일 저장에 실패하였습니다.");
            }
        }
    }

    public String extract_data(BufferedImage img){

        StringBuffer s = new StringBuffer();
        StringBuffer o = new StringBuffer();

        for(int i = 0 ; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color c = new Color(img.getRGB(j, i));
                s.append(c.getRed() % 2);
                s.append(c.getGreen() % 2);
                s.append(c.getBlue() % 2);
            }
        }
        int index = 0;
        while (true){
            int ascii = Integer.parseInt(s.substring(index * 8, index * 8 + 8), 2);
            if (ascii > 128)
                break;
            o.append((char)ascii);
            index++;
            System.out.println(o);
        }
        if (o.length() == 0)
            return "불러올 수 있는 데이터가 존재하지 않습니다";
        return o.toString();
    }
    class MenuActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            if (cmd.equals(filemenu_String[0])) {
                // 파일 open 코드

                JFileChooser file_chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "gif", "png", "bmp", "jpeg");
                file_chooser.setFileFilter(filter);

                int users_value = file_chooser.showOpenDialog(file_chooser);

                if (users_value == JFileChooser.APPROVE_OPTION) {
                    File picture = file_chooser.getSelectedFile();
                    try {
                        user_img = ImageIO.read(picture);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    imgLabel.setText("");
                    filtered_img = user_img;
                    setImageView();
                }
            } else if (cmd.equals(filemenu_String[1])) {
                img_save();
            } else if (cmd.equals(filemenu_String[2])) {
                // 프로그램 종료
                System.exit(0);
            } else if (cmd.equals(insertmenu_String[0])) {
                // Text 프롬프트
                msgbox("[주의]텍스트를 넣으신뒤 필터설정을 바꾸시게 되면 다시 텍스트를 넣어야 합니다.");
                text_dialog.setVisible(true);
                insert_text.setLineWrap(true);

            } else if (cmd.equals("Extract Text")) {
                String text = extract_data(user_img);
                msgbox(text);
            } else if (cmd.equals("About Us")) {
                about_dialog.setVisible(true);
            }
        }
    }
    // 메인메서드
    public static void main(String[] args) {
        new Main();
    }

}

