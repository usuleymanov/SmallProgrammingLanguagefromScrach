import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.border.EtchedBorder;
import javax.swing.JScrollPane;

import typechecking.TypeException;

import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;

public class Main {
	/*
	 * Lexer class - tokenizes the given input
	 * Parser class - constructs AST
	 * Evaluate class - calculates the values of expressions and stores the results in State class
	 * when it encounters assignment statement. Uses Scope class to push and pop values from State.
	 * Scope class - used by Evaluate class for handling scope of variables
	 * State class - stores the identifier and value of given variable. For every variable we have 
	 * stack of values because of scoping. 
	 * TypeException class- thrown when program has type error.
	 * SyntaxErrorException - thrown when program has syntax error.
	 */

	private JFrame frame;
	private JTextPane editorCode;
	private JTextPane editorConsole;
	private Lexer lexer;
	private Parser parser;
	public Evaluate eval;
	public Program pr;
	Highlighter highlighter;
	HighlightPainter painter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		lexer = new Lexer();
		eval = new Evaluate();
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 50, 900, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnfile = new JMenu("File");
		menuBar.add(mnfile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
		mnfile.add(mntmExit);

		JToolBar toolBar = new JToolBar();
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);

		JButton btnRun = new JButton("Execute");
		ImageIcon runIcon = new ImageIcon(
				Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/execute.png"))
						.getScaledInstance(24, 24, Image.SCALE_SMOOTH));
		btnRun.setIcon(runIcon);
		toolBar.add(btnRun);
        btnRun.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					eval.evaluateProgram(pr);
					editorConsole.setText(eval.printVlaues());
				} catch (TypeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					editorConsole.setText(e.msg);
				}
				//editorConsole.setText(eval.printVlaues());
			}
        	
        });;
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		splitPane.setOneTouchExpandable(true);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);

		editorCode = new JTextPane();
		highlighter = editorCode.getHighlighter();
	    painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
		editorCode.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				highlight(e);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				highlight(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});
		editorCode.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		editorCode.setMinimumSize(new Dimension(0, 500));
		splitPane.setLeftComponent(editorCode);

		editorConsole = new JTextPane();
		editorConsole.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		splitPane.setRightComponent(editorConsole);
	}

	protected void highlight(final DocumentEvent e) {
		Runnable doHighlight = new Runnable() {
			@Override
			public void run() {
				try {
					Document doc = e.getDocument();
					String code = doc.getText(0, doc.getLength());

					ArrayList<Token> tokens = lexer.lex(code);
				    parser = new Parser(tokens);
					
					try {
					    pr = parser.parsePrg();
						editorConsole.setText("");
						//remove all highlighting because we have not encountered syntax exception 
						highlighter.removeAllHighlights();
						
					}
					catch(SyntaxErrorException e){
						//highlight the previous token which has syntax error
						editorConsole.setText(e.msg);
						System.out.println(e.msg);
						highlighter.removeAllHighlights();
					   highlighter.addHighlight(e.prev.getPosition(), e.prev.getPosition() + e.prev.getData().length(), painter );
					}
					
					HashMap<String, Color> tokenColors = new HashMap<>();
					tokenColors.put(TokenType.FLOATVALUE.name(), Color.ORANGE);
					tokenColors.put(TokenType.NUMBER.name(), Color.GREEN);
					tokenColors.put(TokenType.IDENT.name(), Color.RED);
					
					HashMap<String, Style> tokenStyles = new HashMap<>();
					for (String key : tokenColors.keySet()) {
						Color color = tokenColors.get(key);
						Style style = editorCode.addStyle(key, null);
						StyleConstants.setForeground(style, color);
						tokenStyles.put(key, style);
					}
					
					Style defaultStyle = editorCode.addStyle("default", null);
					StyleConstants.setForeground(defaultStyle, Color.BLACK);

					// now iterate through tokens and try to highlight them
					// see the example below

					for (Token token : tokens) {
						Style tokenStyle = tokenStyles.get(token.getType().name());
						if (tokenStyle != null)
							editorCode
								.getStyledDocument()
								.setCharacterAttributes(token.getPosition(), token.getData().length(), tokenStyle, true);
						else
							editorCode
								.getStyledDocument()
								.setCharacterAttributes(token.getPosition(), token.getData().length(), defaultStyle, true);
					}
				} catch (BadLocationException e1) {
					// exception
				}
			}
		};
		SwingUtilities.invokeLater(doHighlight);
	}
}