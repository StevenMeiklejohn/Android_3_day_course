### Calculator App

Learning Objectives: Widgets, constraint layouts, layout orientation, positioning, common properties, on-click listeners (multiple).


The first thing we're going to do is create a basic calculator app to familiarise ourselves with some basics.

- Open Android Studio and click on New Project.
- Call it Calculator
- Select Empty Activity.

### Button Layout.

In the Res/Layouts folder, open up the activity_main.xml.
In Android studio, activities represent screens or pages on the user device. They have two views;
The first shows the XML which is being used to populate the screen. This is a bit like HTML in a web page. Objects, or widgets, can be added and edited directly using XML in much the same way as we would with HTML.
The second view is a GUI. This view allows us to drag and drop widgets directly on to the activity, position them, re-size them etc all using the mouse. For convenience, we will be focusing on this method though its a good idea to familiarise yourself with the XML code too.


- Check that the default margin setting is set to 8dp (located just above the activity pane).
- Drag and drop a new Text/Number(signed) widget to the activity. This will be our calculator's results  field.
- Using the properties pane on the right hand side, change the id to 'Result'.
- Constrain the result widget to the top, left and right by clicking on the corresponding dots and dragging to the respective sides. You will notice it is now centred horizontally.
- Using the properties pane on the right hand side, check that the layout length and width is set to wrap content both horizontally and vertically.

- Next, drag and drop 16 buttons onto the activity. Don't worry about their placement for the moment as we will constrain them in a grid later.
- Change the button text to reflect the id number android studio has assigned. EG. Button2 will have text 2, button 10 will have text 1, etc. Buttons with ID > 10 will be our *,/,+,-
(You might wonder why we wouldn't just change the IDs and text to reflect where our buttons are placed, but AS will not allow us to change a button to an ID which already exists).
- Ok, lets fix up our placement. Button 7 will be our 'key' button, meaning, all the other buttons will reference its position. So constrain the left of button 7 to the left of the screen.
- Constrain the left of the number 8 button to the right of the seven button
- Select button 8 and click 'ab'. Click the horizontal line that appears in the button and connect it to the same line in number 7. This constrains the two baselines together.
- Repeat the process for button 9.

- Connect the left of button 4 to the left of button 7 and change the margin of button 4 to 0. It should now line up vertically with button 7. Constrain the top of button 4 to the bottom of button 7.
- Carry on in this way till all number buttons are placed and can be dragged around as a single object.
- Select all the buttons and change the minWidth property to 48dp. The button size should reduce

- Lets add our two missing widgets;
- We need another textview and a text/number(signed)*. *EditText
- Drag both onto the activity.
- Change the id of the new textView to 'operation'.
- Change the id of the new EditText to 'newNumber'.
- Constrain the left of operation to the left of the screen and the left of newNumber
- Constrain the baseline of operation and newNumber together.
- Constrain the top of newNumber to the bottom of result. Constrain the left of newNumber to the left of the screen and the right to the right. This should centre the widget horizontally.
- Remove the text from operation so that it is invisible unless clicked.

### Layouts and Device Orientation.

You might notice when you switch into landscape view (top left of the design pane) that the buttons appear too close to the bottom. We could reduce the spacing between the buttons, but Android studio allows us to define different layouts for different screen sizes (landscape mode counts as a different screen size).
To the right of 'language' in the design panel there is an icon that looks like two overlapping rectangles. Click this to see the drop down and menu and select 'create landscape variation'. This creates a new landscape layout. (These can be seen in projects/res/layout).

- Move the button layout configuration (by adjusting the constraints) as you see fit.

You should now be able to click the 'play' icon, select a virtual device and check what the app will look like when deployed.


### OnClick Listeners (Numbers).

You will notice when we run our app on the emulator that buttons appear as expected but don't do anything. The first step in linking our buttons to come actual is via  onClickListeners.
We will be setting an onClick Listener on each of our buttons. When the app initialises the listeners are set and wait for any input. A bit like a mouse trap. Once they are triggered they can execute whatever piece of code we have assigned. This works in a very similar fashion to listeners in javascript.

So, what is the flow we need to implement to build our calculator?

1. Trigger onClickListener
2. Decide if button is a number or an operation. (We will do this by using two different types of listener, as opposed to using logic/if statement).
3. If the button clicked is a number it is appended to number being built, if it is an operation it is stored as a pending operation and update appear on screen to remind the user that an operation is pending.
4. Once an operation is performed (i.e. number - operation - number), the result will appear in the result widget.

Ok, lets write some code.....

Open the MainActivity file. You should see that we have one method already. The onCreate method is the first thing the app does when started. The file containing the onCreate is generally named 'main'. As standard the onCreate checks for any saved states and then defines which activity to display (in this case the activity_main view we working on previously).
So, the first thing we need to do is declare and define or widgets.

```
public class MainActivity extends AppCompatActivity {

    private EditText result;
    private EditText newNumber;
    private TextView displayOperation;

//    Variable to hold the operands and type of calculations.
    private Double operand1 = null;
    private Double operand2 = null;
    private String pendingOperation = "=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (EditText) findViewById(R.id.result);
        newNumber = (EditText) findViewById(R.id.newNumber);
        displayOperation = (TextView) findViewById(R.id.operation);
    }
}
```

Now, we need to set up our buttons;

```
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    result = (EditText) findViewById(R.id.result);
    newNumber = (EditText) findViewById(R.id.newNumber);
    displayOperation = (TextView) findViewById(R.id.operation);

    Button button0 = (Button) findViewById(R.id.button0);
    Button button1 = (Button) findViewById(R.id.button1);
    Button button2 = (Button) findViewById(R.id.button2);
    Button button3 = (Button) findViewById(R.id.button3);
    Button button4 = (Button) findViewById(R.id.button4);
    Button button5 = (Button) findViewById(R.id.button5);
    Button button6 = (Button) findViewById(R.id.button6);
    Button button7 = (Button) findViewById(R.id.button7);
    Button button8 = (Button) findViewById(R.id.button8);
    Button button9 = (Button) findViewById(R.id.button9);
    Button buttonDivide = (Button) findViewById(R.id.buttonDivide);
    Button buttonMultiply = (Button) findViewById(R.id.buttonMultiply);
    Button buttonMinus = (Button) findViewById(R.id.buttonMinus);
    Button buttonDecimal = (Button) findViewById(R.id.buttonDecimal);
    Button buttonEquals = (Button) findViewById(R.id.buttonEquals);
    Button buttonPlus = (Button) findViewById(R.id.buttonPlus);
```

Next, (still in the onCreate method), lets set an OnCLickListener on our View. We will tell the listener to obtain the value of the button pressed and append it to our newNumber variable (thus reflecting the user input).

```
View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View view){
              Button b = (Button) view;
              newNumber.append(b.getText().toString());
            }
        }
```
How does this work?
All android widgets are views. When the OnClickListener is triggered it passes whichever view was clicked to the OnClick method. We then cast this view to a button object (since we will attach this listener to buttons only) and use it to retrieve the text (value) of that button.

Next, we need to assign this listener to our buttons 0 through 9.

```
button0.setOnClickListener(listener);
button1.setOnClickListener(listener);
button2.setOnClickListener(listener);
button3.setOnClickListener(listener);
button4.setOnClickListener(listener);
button5.setOnClickListener(listener);
button6.setOnClickListener(listener);
button7.setOnClickListener(listener);
button8.setOnClickListener(listener);
button9.setOnClickListener(listener);
buttonDecimal.setOnClickListener(listener);
```

*Note: To save a bit of time we could have added these buttons to an array then looped through it to assign the OnClickListener to each of them.

Lets run the emulator again and check everything is still working.
You will notice clicking decimal a number of times only produces one dot on the screen. This is because we set the button to be decimal when we were laying out the activity, so Android is basically taking care of this for us.



### OnClick Listeners (Operators).

Lets make our OnClick Listeners for the operator buttons.

(Still in onCreate)
```
View.OnClickListener opListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button) view;
                String op = b.getText().toString();
                String value = newNumber.getText().toString();
                try {
                    Double doubleValue = Double.valueOf(value);
                    performOperation(doubleValue, op);
                } catch(NumberFormatException e) {
                    newNumber.setText("");

                }
                pendingOperation = op;
                displayOperation.setText(pendingOperation);
            }
        };
```
What's going on?
As before we are casting the view which has been passed to the onclick method to a button object. From this button object we can acquire the value of the button pressed.
We convert our String 'value' to a double.
Next, we assign the newNumber variable (converted to a string) to value.
Next, we use a try/catch to mitigate the crash that would be caused by entering something which is not a number (like decimal). In this case, we set the display to 0.
If all is well, we call the performOperation method. (We will write the performOperation method shortly).
We assign the operation of the pressed button to pendingOperation and update the our view to display it.

Ok, next we must set the onClick listener we just created to our operation buttons.

```
buttonEquals.setOnClickListener(opListener);
buttonPlus.setOnClickListener(opListener);
buttonMinus.setOnClickListener(opListener);
buttonMultiply.setOnClickListener(opListener);
buttonDivide.setOnClickListener(opListener);
```


Ok, lets write our missing performOperation() method. (As you would expect, we will write this outside the onCreate method).
The included comments should describe what is happening;

```
public void performOperation(String value, String operation){
//        if operand1 is null, value is assigned to operand1. If not, value is assigned to operand2. This removes the need for a clear button, as pressing equals effectively resets the calculation.
    if(operand1 == null){
        operand1 = value;
    } else {
        operand2 = value;
//          if the currently pending operation is 'equals', re-assign it to the operation passed in.
        if (pendingOperation.equals("=")) {
            pendingOperation = operation;
        }
//          Switch statement defines which calculation to make based on the pending operation.
        switch (pendingOperation) {
            case "=":
                operand1 = operand2;
                break;
            case "/":
//                    Check if user has tried to divide by 0 and if so, return 0.0
                if(operand2 == 0){
                    operand1 = 0.0;
                } else {
                    operand1 /= operand2;
                }
                break;
            case "*":
                operand1 *= operand2;
                break;
            case "-":
                operand1 -= operand2;
                break;
            case "+":
                operand1 += operand2;
                break;
        }
    }
//        Display the result of calculation
    result.setText(operand1.toString());
//        Reset new number variable.
    newNumber.setText("");
}
```

Run the application on the emulator and check everything is working.
Clearly there are still a couple of issues that need to be ironed out, such as possibly adding a 'negative' to allow the input of....negative numbers, but that's more of an exercise in Java.
So, we have now built our first android app which has introduced us to;
Activities.
Widgets (buttons, textView, etc).
OnClickListeners.
The 'main' method.
