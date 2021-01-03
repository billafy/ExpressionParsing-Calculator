import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Calculator implements ActionListener
{
	private String expression;
	Label screen = new Label();
	boolean evaluated = false;
	A3_Calculator()
	{
		expression = "";
		Frame frame = new Frame("Bawal Billa Calculator");

		screen = new Label();
		screen.setBounds(15,15,270,75);
		screen.setBackground(Color.WHITE);
		frame.add(screen);
		
		Button[] digits = new Button[9];
		int x = 155,y = 100,num = 9;
		for(int i=1;i<10;++i)
		{
			digits[i-1] = new Button(Integer.toString(num));
			digits[i-1].setBounds(x,y,60,60);
			digits[i-1].addActionListener(this);
			--num;
			frame.add(digits[i-1]);
			x -= 70;
			if(num%3==0)
			{
				x = 155;
				y += 70;
			}
		}
		Button zero = new Button("0");
		zero.setBounds(15,310,60,60);
		zero.addActionListener(this);
		frame.add(zero);

		Button back = new Button("<--");
		Button add = new Button("+");
		Button sub = new Button("-");
		Button mul = new Button("*");
		Button div = new Button("/");
		Button equal = new Button("=");
		back.setBounds(225,100,60,60); back.addActionListener(this);
		add.setBounds(225,170,60,60); add.addActionListener(this);
		sub.setBounds(225,240,60,60); sub.addActionListener(this);
		mul.setBounds(225,310,60,60); mul.addActionListener(this);
		div.setBounds(155,310,60,60); div.addActionListener(this);
		equal.setBounds(85,310,60,60); equal.addActionListener(this);
		frame.add(back);
		frame.add(add);
		frame.add(sub);
		frame.add(mul);
		frame.add(div);
		frame.add(equal);

		frame.setBackground(Color.BLACK);
		frame.setSize(300,400);
		frame.setLayout(null);
		frame.setVisible(true);
	}
	private String getNumber(String expression,int j)
	{
		String num = "";
		num += Character.toString(expression.charAt(j-1));
		for(int i=j;i<expression.length();++i)
		{
			String nextChar = Character.toString(expression.charAt(i));
			try
			{
				int temp = Integer.parseInt(nextChar);
				num += nextChar;
			}
			catch(Exception e)
			{
				break;
			}
		}
		return num;
	}
	private boolean isNumeric(char x)
	{
		try
		{
			int temp = Integer.parseInt(Character.toString(x));
			return true;
		}
		catch(Exception e) {}
		return false;
	}
	private boolean syntaxError()
	{
		if(!isNumeric(expression.charAt(0)) || !isNumeric(expression.charAt(expression.length()-1)))
			return true;
		for(int i=1;i<expression.length();++i)
		{
			if(!isNumeric(expression.charAt(i-1)) && !isNumeric(expression.charAt(i)))
				return true;
		}
		return false;
	}
	private void evaluateExpression()
	{
		if(expression.length()==0)
			return;
		if(syntaxError())
		{
			screen.setText("Syntax Error");
			expression = "";
			evaluated = true;
			return;
		}
		ArrayList<Double> numbers = new ArrayList<Double>();
		ArrayList<String> operators = new ArrayList<String>();
		String num = "";
		int i = 0;
		while(i<expression.length())
		{
			String nextChar = Character.toString(expression.charAt(i));
			try
			{
				int temp = Integer.parseInt(nextChar);
				if(operators.size()>0)
				{
					if(operators.get(operators.size()-1).equals("*"))
					{						
						String num2 = getNumber(expression,i+1);
						double result = numbers.get(numbers.size()-1)*Double.parseDouble(num2);
						operators.remove(operators.size()-1);
						numbers.remove(numbers.size()-1);
						numbers.add(result);
						i = i + num2.length();
					}
					else if(operators.get(operators.size()-1).equals("/"))
					{
						String num2 = getNumber(expression,i+1);
						double result = numbers.get(numbers.size()-1)/Double.parseDouble(num2);
						operators.remove(operators.size()-1);
						numbers.remove(numbers.size()-1);
						numbers.add(result);
						i = i + num2.length();
					}
					else
					{
						String num2 = getNumber(expression,i+1);
						numbers.add(Double.parseDouble(num2));
						i = i + num2.length();
					}
				}
				else
				{
					String num2 = getNumber(expression,i+1);
					numbers.add(Double.parseDouble(num2));
					i = i + num2.length();
				}
			}	
			catch(Exception e)
			{
				operators.add(nextChar);
				++i;
			}		
		}
		int start = 0, end = 1;
		for(i=0;i<operators.size();++i)
		{
			double temp = 0;
			if((operators.get(i)).equals("+"))
			{				
				temp = numbers.get(start) + numbers.get(end);
			}
			else if((operators.get(i)).equals("-"))
			{
				temp = numbers.get(start) - numbers.get(end);
			}
			numbers.set(end,temp);
			++start;
			++end;
		}
		String result = String.format("%f",numbers.get(start));
		i = result.length() - 1;
		while(true)
		{
			if(result.charAt(i)!='0')
				break;
			else
			{
				if(result.charAt(i-1)=='.')
				{
					result = result.substring(0,result.length()-2);
					break;
				}
				else
					result = result.substring(0,result.length()-1);
			}
			--i;
		}
		screen.setText(result);
		expression = "";
		evaluated = true;
	}
	public void actionPerformed(ActionEvent event)
	{
		if(evaluated)
		{
			evaluated = false;
			screen.setText("");
		}
		if(expression.length()==35)
			return;
		if((((Button)event.getSource()).getLabel()).equals("="))
		{
			evaluateExpression();
			return;
		}
		if((((Button)event.getSource()).getLabel()).equals("<--"))
		{
			if(expression.length()==0)
			{
				screen.setText("");
				return;
			}
			expression = expression.substring(0,expression.length()-1);
			screen.setText(expression);
			return;
		}
		String nextChar = ((Button)event.getSource()).getLabel();
		expression += nextChar; 
		screen.setText(expression);
	}
	public static void main(String[] args) 
	{
		Calculator calculator = new Calculator();		
	}
}