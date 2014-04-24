import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class MenuOption extends TransparentPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MenuOption() {
		setLayout(null);
		TransparentPanel panel = new TransparentPanel();
		panel.setBackground(Color.DARK_GRAY);
		panel.setBounds(0, 0, 100, 80);
		add(panel);
		setBackground(Color.WHITE);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		TransparentPanel panel_3 = new TransparentPanel();
		panel_3.setBackground(Color.BLACK);
		panel.add(panel_3);
		panel_3.setLayout(new BorderLayout(5, 5));
		JButton btnNewButton = new JButton("Option");
		btnNewButton.setOpaque(false);
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBackground(Color.BLACK);
		panel_3.add(btnNewButton);
		btnNewButton.setAlignmentY(Component.TOP_ALIGNMENT);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Slime.isInMenu=false;
				Slime.frame.requestFocus();
				setVisible(false);
			}
		});
		
		TransparentPanel panel_2 = new TransparentPanel();
		panel_2.setBackground(Color.BLACK);
		panel.add(panel_2);
		panel_2.setLayout(new BorderLayout(5, 5));
		
		JButton btnNewButton_2 = new JButton("Exit  Game");
		btnNewButton_2.setOpaque(false);
		btnNewButton_2.setBackground(Color.BLACK);
		btnNewButton_2.setForeground(Color.WHITE);
		panel_2.add(btnNewButton_2);
		btnNewButton_2.setAlignmentY(Component.TOP_ALIGNMENT);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		TransparentPanel panel_1 = new TransparentPanel();
		panel_1.setBackground(Color.BLACK);
		panel.add(panel_1);
		panel_1.setLayout(new BorderLayout(5, 5));
		
		JButton btnNewButton_1 = new JButton("Resume");
		btnNewButton_1.setOpaque(false);
		btnNewButton_1.setForeground(Color.WHITE);
		btnNewButton_1.setBackground(Color.BLACK);
		panel_1.add(btnNewButton_1, BorderLayout.CENTER);
		btnNewButton_1.setAlignmentY(Component.TOP_ALIGNMENT);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Slime.isInMenu=false;
				Slime.frame.requestFocus();
				setVisible(false);
			}
		});
		//setVisible(false);
	}

}
