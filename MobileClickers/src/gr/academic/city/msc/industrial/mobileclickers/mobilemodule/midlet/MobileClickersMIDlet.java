/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.academic.city.msc.industrial.mobileclickers.mobilemodule.midlet;

import gr.academic.city.msc.industrial.mobileclickers.mobilemodule.generated.ws.MobileClickersWSService;
import gr.academic.city.msc.industrial.mobileclickers.mobilemodule.generated.ws.MobileClickersWSService_Stub;
import java.rmi.RemoteException;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import org.netbeans.microedition.lcdui.SplashScreen;
import org.netbeans.microedition.lcdui.WaitScreen;
import org.netbeans.microedition.util.SimpleCancellableTask;

/**
 * @author Ivo Neskovic <ivo.neskovic@gmail.com>
 */
public class MobileClickersMIDlet extends MIDlet implements CommandListener {

    private boolean midletPaused = false;
    private MobileClickersWSService webService;
    private String questionCode;
    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private Form selectQuestionForm;
    private TextField questionCodeTextField;
    private WaitScreen getQuestionWaitScreen;
    private WaitScreen submitAnswerWaitScreen;
    private List answersList;
    private Alert getQuestionErrorAlert;
    private Alert submitAnswerErrorAlert;
    private Alert submitAnswerSuccessAlert;
    private SplashScreen splashScreen;
    private Command exitCommand;
    private Command getQuestionCommand;
    private Command submitAnswerCommand;
    private SimpleCancellableTask getQuestionTask;
    private SimpleCancellableTask submitAnswerTask;
    private Image clock;
    private Image success;
    private Image error;
    private Image logo;
    //</editor-fold>//GEN-END:|fields|0|

    /**
     * The MobileClickersMIDlet constructor.
     */
    public MobileClickersMIDlet() {
    }

    //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    //</editor-fold>//GEN-END:|methods|0|
    //<editor-fold defaultstate="collapsed" desc=" Generated Method: initialize ">//GEN-BEGIN:|0-initialize|0|0-preInitialize
    /**
     * Initilizes the application.
     * It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
     */
    private void initialize() {//GEN-END:|0-initialize|0|0-preInitialize
        // write pre-initialize user code here
//GEN-LINE:|0-initialize|1|0-postInitialize
        // write post-initialize user code here
    }//GEN-BEGIN:|0-initialize|2|
    //</editor-fold>//GEN-END:|0-initialize|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: startMIDlet ">//GEN-BEGIN:|3-startMIDlet|0|3-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {//GEN-END:|3-startMIDlet|0|3-preAction
        // write pre-action user code here
        switchDisplayable(null, getSplashScreen());//GEN-LINE:|3-startMIDlet|1|3-postAction
        // write post-action user code here
    }//GEN-BEGIN:|3-startMIDlet|2|
    //</editor-fold>//GEN-END:|3-startMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: resumeMIDlet ">//GEN-BEGIN:|4-resumeMIDlet|0|4-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     */
    public void resumeMIDlet() {//GEN-END:|4-resumeMIDlet|0|4-preAction
        // write pre-action user code here
//GEN-LINE:|4-resumeMIDlet|1|4-postAction
        // write post-action user code here
    }//GEN-BEGIN:|4-resumeMIDlet|2|
    //</editor-fold>//GEN-END:|4-resumeMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: switchDisplayable ">//GEN-BEGIN:|5-switchDisplayable|0|5-preSwitch
    /**
     * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
     * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {//GEN-END:|5-switchDisplayable|0|5-preSwitch
        // write pre-switch user code here
        Display display = getDisplay();//GEN-BEGIN:|5-switchDisplayable|1|5-postSwitch
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }//GEN-END:|5-switchDisplayable|1|5-postSwitch
        // write post-switch user code here
    }//GEN-BEGIN:|5-switchDisplayable|2|
    //</editor-fold>//GEN-END:|5-switchDisplayable|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: commandAction for Displayables ">//GEN-BEGIN:|7-commandAction|0|7-preCommandAction
    /**
     * Called by a system to indicated that a command has been invoked on a particular displayable.
     * @param command the Command that was invoked
     * @param displayable the Displayable where the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:|7-commandAction|0|7-preCommandAction
        // write pre-action user code here
        if (displayable == answersList) {//GEN-BEGIN:|7-commandAction|1|47-preAction
            if (command == List.SELECT_COMMAND) {//GEN-END:|7-commandAction|1|47-preAction
                // write pre-action user code here
                answersListAction();//GEN-LINE:|7-commandAction|2|47-postAction
                // write post-action user code here
            } else if (command == exitCommand) {//GEN-LINE:|7-commandAction|3|53-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|4|53-postAction
                // write post-action user code here
            } else if (command == submitAnswerCommand) {//GEN-LINE:|7-commandAction|5|50-preAction
                // write pre-action user code here
                switchDisplayable(null, getSubmitAnswerWaitScreen());//GEN-LINE:|7-commandAction|6|50-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|7|27-preAction
        } else if (displayable == getQuestionWaitScreen) {
            if (command == WaitScreen.FAILURE_COMMAND) {//GEN-END:|7-commandAction|7|27-preAction
                // write pre-action user code here
                switchDisplayable(getGetQuestionErrorAlert(), getSelectQuestionForm());//GEN-LINE:|7-commandAction|8|27-postAction
                // write post-action user code here
            } else if (command == WaitScreen.SUCCESS_COMMAND) {//GEN-LINE:|7-commandAction|9|26-preAction
                // write pre-action user code here
                switchDisplayable(null, getAnswersList());//GEN-LINE:|7-commandAction|10|26-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|11|17-preAction
        } else if (displayable == selectQuestionForm) {
            if (command == exitCommand) {//GEN-END:|7-commandAction|11|17-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|12|17-postAction
                // write post-action user code here
            } else if (command == getQuestionCommand) {//GEN-LINE:|7-commandAction|13|21-preAction
                // write pre-action user code here
                switchDisplayable(null, getGetQuestionWaitScreen());//GEN-LINE:|7-commandAction|14|21-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|15|65-preAction
        } else if (displayable == splashScreen) {
            if (command == SplashScreen.DISMISS_COMMAND) {//GEN-END:|7-commandAction|15|65-preAction
                // write pre-action user code here
                switchDisplayable(null, getSelectQuestionForm());//GEN-LINE:|7-commandAction|16|65-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|17|36-preAction
        } else if (displayable == submitAnswerWaitScreen) {
            if (command == WaitScreen.FAILURE_COMMAND) {//GEN-END:|7-commandAction|17|36-preAction
                // write pre-action user code here
                switchDisplayable(getSubmitAnswerErrorAlert(), getSelectQuestionForm());//GEN-LINE:|7-commandAction|18|36-postAction
                // write post-action user code here
            } else if (command == WaitScreen.SUCCESS_COMMAND) {//GEN-LINE:|7-commandAction|19|35-preAction
                // write pre-action user code here
                switchDisplayable(getSubmitAnswerSuccessAlert(), getSelectQuestionForm());//GEN-LINE:|7-commandAction|20|35-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|21|7-postCommandAction
        }//GEN-END:|7-commandAction|21|7-postCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|7-commandAction|22|
    //</editor-fold>//GEN-END:|7-commandAction|22|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: selectQuestionForm ">//GEN-BEGIN:|14-getter|0|14-preInit
    /**
     * Returns an initiliazed instance of selectQuestionForm component.
     * @return the initialized component instance
     */
    public Form getSelectQuestionForm() {
        if (selectQuestionForm == null) {//GEN-END:|14-getter|0|14-preInit
            // write pre-init user code here
            selectQuestionForm = new Form("Click\'N\'Gage", new Item[] { getQuestionCodeTextField() });//GEN-BEGIN:|14-getter|1|14-postInit
            selectQuestionForm.addCommand(getExitCommand());
            selectQuestionForm.addCommand(getGetQuestionCommand());
            selectQuestionForm.setCommandListener(this);//GEN-END:|14-getter|1|14-postInit
            // write post-init user code here
        }//GEN-BEGIN:|14-getter|2|
        return selectQuestionForm;
    }
    //</editor-fold>//GEN-END:|14-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommand ">//GEN-BEGIN:|16-getter|0|16-preInit
    /**
     * Returns an initiliazed instance of exitCommand component.
     * @return the initialized component instance
     */
    public Command getExitCommand() {
        if (exitCommand == null) {//GEN-END:|16-getter|0|16-preInit
            // write pre-init user code here
            exitCommand = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|16-getter|1|16-postInit
            // write post-init user code here
        }//GEN-BEGIN:|16-getter|2|
        return exitCommand;
    }
    //</editor-fold>//GEN-END:|16-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: questionCodeTextField ">//GEN-BEGIN:|18-getter|0|18-preInit
    /**
     * Returns an initiliazed instance of questionCodeTextField component.
     * @return the initialized component instance
     */
    public TextField getQuestionCodeTextField() {
        if (questionCodeTextField == null) {//GEN-END:|18-getter|0|18-preInit
            // write pre-init user code here
            questionCodeTextField = new TextField("Question code:", null, 32, TextField.ANY);//GEN-LINE:|18-getter|1|18-postInit
            // write post-init user code here
        }//GEN-BEGIN:|18-getter|2|
        return questionCodeTextField;
    }
    //</editor-fold>//GEN-END:|18-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: getQuestionWaitScreen ">//GEN-BEGIN:|23-getter|0|23-preInit
    /**
     * Returns an initiliazed instance of getQuestionWaitScreen component.
     * @return the initialized component instance
     */
    public WaitScreen getGetQuestionWaitScreen() {
        if (getQuestionWaitScreen == null) {//GEN-END:|23-getter|0|23-preInit
            // write pre-init user code here
            getQuestionWaitScreen = new WaitScreen(getDisplay());//GEN-BEGIN:|23-getter|1|23-postInit
            getQuestionWaitScreen.setTitle("Fetching question...");
            getQuestionWaitScreen.setCommandListener(this);
            getQuestionWaitScreen.setImage(getClock());
            getQuestionWaitScreen.setText("Please wait...");
            getQuestionWaitScreen.setTask(getGetQuestionTask());//GEN-END:|23-getter|1|23-postInit
            // write post-init user code here
        }//GEN-BEGIN:|23-getter|2|
        return getQuestionWaitScreen;
    }
    //</editor-fold>//GEN-END:|23-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: getQuestionCommand ">//GEN-BEGIN:|20-getter|0|20-preInit
    /**
     * Returns an initiliazed instance of getQuestionCommand component.
     * @return the initialized component instance
     */
    public Command getGetQuestionCommand() {
        if (getQuestionCommand == null) {//GEN-END:|20-getter|0|20-preInit
            // write pre-init user code here
            getQuestionCommand = new Command("Get Question", Command.SCREEN, 0);//GEN-LINE:|20-getter|1|20-postInit
            // write post-init user code here
        }//GEN-BEGIN:|20-getter|2|
        return getQuestionCommand;
    }
    //</editor-fold>//GEN-END:|20-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: getQuestionTask ">//GEN-BEGIN:|28-getter|0|28-preInit
    /**
     * Returns an initiliazed instance of getQuestionTask component.
     * @return the initialized component instance
     */
    public SimpleCancellableTask getGetQuestionTask() {
        if (getQuestionTask == null) {//GEN-END:|28-getter|0|28-preInit
            // write pre-init user code here
            getQuestionTask = new SimpleCancellableTask();//GEN-BEGIN:|28-getter|1|28-execute
            getQuestionTask.setExecutable(new org.netbeans.microedition.util.Executable() {
                public void execute() throws Exception {//GEN-END:|28-getter|1|28-execute
                    // write task-execution user code here
                    getNumberOfAnswers();
                }//GEN-BEGIN:|28-getter|2|28-postInit
            });//GEN-END:|28-getter|2|28-postInit
            // write post-init user code here
        }//GEN-BEGIN:|28-getter|3|
        return getQuestionTask;
    }
    //</editor-fold>//GEN-END:|28-getter|3|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: submitAnswerWaitScreen ">//GEN-BEGIN:|34-getter|0|34-preInit
    /**
     * Returns an initiliazed instance of submitAnswerWaitScreen component.
     * @return the initialized component instance
     */
    public WaitScreen getSubmitAnswerWaitScreen() {
        if (submitAnswerWaitScreen == null) {//GEN-END:|34-getter|0|34-preInit
            // write pre-init user code here
            submitAnswerWaitScreen = new WaitScreen(getDisplay());//GEN-BEGIN:|34-getter|1|34-postInit
            submitAnswerWaitScreen.setTitle("Submitting answer...");
            submitAnswerWaitScreen.setCommandListener(this);
            submitAnswerWaitScreen.setImage(getClock());
            submitAnswerWaitScreen.setText("Please wait...");
            submitAnswerWaitScreen.setTask(getSubmitAnswerTask());//GEN-END:|34-getter|1|34-postInit
            // write post-init user code here
        }//GEN-BEGIN:|34-getter|2|
        return submitAnswerWaitScreen;
    }
    //</editor-fold>//GEN-END:|34-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: submitAnswerCommand ">//GEN-BEGIN:|38-getter|0|38-preInit
    /**
     * Returns an initiliazed instance of submitAnswerCommand component.
     * @return the initialized component instance
     */
    public Command getSubmitAnswerCommand() {
        if (submitAnswerCommand == null) {//GEN-END:|38-getter|0|38-preInit
            // write pre-init user code here
            submitAnswerCommand = new Command("Submit", Command.SCREEN, 0);//GEN-LINE:|38-getter|1|38-postInit
            // write post-init user code here
        }//GEN-BEGIN:|38-getter|2|
        return submitAnswerCommand;
    }
    //</editor-fold>//GEN-END:|38-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: submitAnswerTask ">//GEN-BEGIN:|37-getter|0|37-preInit
    /**
     * Returns an initiliazed instance of submitAnswerTask component.
     * @return the initialized component instance
     */
    public SimpleCancellableTask getSubmitAnswerTask() {
        if (submitAnswerTask == null) {//GEN-END:|37-getter|0|37-preInit
            // write pre-init user code here
            submitAnswerTask = new SimpleCancellableTask();//GEN-BEGIN:|37-getter|1|37-execute
            submitAnswerTask.setExecutable(new org.netbeans.microedition.util.Executable() {
                public void execute() throws Exception {//GEN-END:|37-getter|1|37-execute
                    // write task-execution user code here
                    submitAnswer();
                }//GEN-BEGIN:|37-getter|2|37-postInit
            });//GEN-END:|37-getter|2|37-postInit
            // write post-init user code here
        }//GEN-BEGIN:|37-getter|3|
        return submitAnswerTask;
    }
    //</editor-fold>//GEN-END:|37-getter|3|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: clock ">//GEN-BEGIN:|44-getter|0|44-preInit
    /**
     * Returns an initiliazed instance of clock component.
     * @return the initialized component instance
     */
    public Image getClock() {
        if (clock == null) {//GEN-END:|44-getter|0|44-preInit
            // write pre-init user code here
            try {//GEN-BEGIN:|44-getter|1|44-@java.io.IOException
                clock = Image.createImage("/gr/academic/city/msc/industrial/mobileclickers/mobilemodule/images/clock.png");
            } catch (java.io.IOException e) {//GEN-END:|44-getter|1|44-@java.io.IOException
                e.printStackTrace();
            }//GEN-LINE:|44-getter|2|44-postInit
            // write post-init user code here
        }//GEN-BEGIN:|44-getter|3|
        return clock;
    }
    //</editor-fold>//GEN-END:|44-getter|3|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: answersList ">//GEN-BEGIN:|45-getter|0|45-preInit
    /**
     * Returns an initiliazed instance of answersList component.
     * @return the initialized component instance
     */
    public List getAnswersList() {
        if (answersList == null) {//GEN-END:|45-getter|0|45-preInit
            // write pre-init user code here
            answersList = new List("Choose your answer:", Choice.EXCLUSIVE);//GEN-BEGIN:|45-getter|1|45-postInit
            answersList.addCommand(getSubmitAnswerCommand());
            answersList.addCommand(getExitCommand());
            answersList.setCommandListener(this);//GEN-END:|45-getter|1|45-postInit
            // write post-init user code here
        }//GEN-BEGIN:|45-getter|2|
        return answersList;
    }
    //</editor-fold>//GEN-END:|45-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: answersListAction ">//GEN-BEGIN:|45-action|0|45-preAction
    /**
     * Performs an action assigned to the selected list element in the answersList component.
     */
    public void answersListAction() {//GEN-END:|45-action|0|45-preAction
        // enter pre-action user code here
        String __selectedString = getAnswersList().getString(getAnswersList().getSelectedIndex());//GEN-LINE:|45-action|1|45-postAction
        // enter post-action user code here
    }//GEN-BEGIN:|45-action|2|
    //</editor-fold>//GEN-END:|45-action|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: getQuestionErrorAlert ">//GEN-BEGIN:|55-getter|0|55-preInit
    /**
     * Returns an initiliazed instance of getQuestionErrorAlert component.
     * @return the initialized component instance
     */
    public Alert getGetQuestionErrorAlert() {
        if (getQuestionErrorAlert == null) {//GEN-END:|55-getter|0|55-preInit
            // write pre-init user code here
            getQuestionErrorAlert = new Alert("Error Fetching Question", "Question with specified code does not exist! Please try again!", getError(), null);//GEN-BEGIN:|55-getter|1|55-postInit
            getQuestionErrorAlert.setTimeout(Alert.FOREVER);//GEN-END:|55-getter|1|55-postInit
            // write post-init user code here
        }//GEN-BEGIN:|55-getter|2|
        return getQuestionErrorAlert;
    }
    //</editor-fold>//GEN-END:|55-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: submitAnswerSuccessAlert ">//GEN-BEGIN:|57-getter|0|57-preInit
    /**
     * Returns an initiliazed instance of submitAnswerSuccessAlert component.
     * @return the initialized component instance
     */
    public Alert getSubmitAnswerSuccessAlert() {
        if (submitAnswerSuccessAlert == null) {//GEN-END:|57-getter|0|57-preInit
            // write pre-init user code here
            submitAnswerSuccessAlert = new Alert("Success!", "Answer submited successfuly!", getSuccess(), null);//GEN-BEGIN:|57-getter|1|57-postInit
            submitAnswerSuccessAlert.setTimeout(Alert.FOREVER);//GEN-END:|57-getter|1|57-postInit
            // write post-init user code here
        }//GEN-BEGIN:|57-getter|2|
        return submitAnswerSuccessAlert;
    }
    //</editor-fold>//GEN-END:|57-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: submitAnswerErrorAlert ">//GEN-BEGIN:|58-getter|0|58-preInit
    /**
     * Returns an initiliazed instance of submitAnswerErrorAlert component.
     * @return the initialized component instance
     */
    public Alert getSubmitAnswerErrorAlert() {
        if (submitAnswerErrorAlert == null) {//GEN-END:|58-getter|0|58-preInit
            // write pre-init user code here
            submitAnswerErrorAlert = new Alert("Error Submiting Answer", "You have already answered this question!", getError(), null);//GEN-BEGIN:|58-getter|1|58-postInit
            submitAnswerErrorAlert.setTimeout(Alert.FOREVER);//GEN-END:|58-getter|1|58-postInit
            // write post-init user code here
        }//GEN-BEGIN:|58-getter|2|
        return submitAnswerErrorAlert;
    }
    //</editor-fold>//GEN-END:|58-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: error ">//GEN-BEGIN:|61-getter|0|61-preInit
    /**
     * Returns an initiliazed instance of error component.
     * @return the initialized component instance
     */
    public Image getError() {
        if (error == null) {//GEN-END:|61-getter|0|61-preInit
            // write pre-init user code here
            try {//GEN-BEGIN:|61-getter|1|61-@java.io.IOException
                error = Image.createImage("/gr/academic/city/msc/industrial/mobileclickers/mobilemodule/images/error_alert.png");
            } catch (java.io.IOException e) {//GEN-END:|61-getter|1|61-@java.io.IOException
                e.printStackTrace();
            }//GEN-LINE:|61-getter|2|61-postInit
            // write post-init user code here
        }//GEN-BEGIN:|61-getter|3|
        return error;
    }
    //</editor-fold>//GEN-END:|61-getter|3|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: success ">//GEN-BEGIN:|62-getter|0|62-preInit
    /**
     * Returns an initiliazed instance of success component.
     * @return the initialized component instance
     */
    public Image getSuccess() {
        if (success == null) {//GEN-END:|62-getter|0|62-preInit
            // write pre-init user code here
            try {//GEN-BEGIN:|62-getter|1|62-@java.io.IOException
                success = Image.createImage("/gr/academic/city/msc/industrial/mobileclickers/mobilemodule/images/success.png");
            } catch (java.io.IOException e) {//GEN-END:|62-getter|1|62-@java.io.IOException
                e.printStackTrace();
            }//GEN-LINE:|62-getter|2|62-postInit
            // write post-init user code here
        }//GEN-BEGIN:|62-getter|3|
        return success;
    }
    //</editor-fold>//GEN-END:|62-getter|3|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: splashScreen ">//GEN-BEGIN:|63-getter|0|63-preInit
    /**
     * Returns an initiliazed instance of splashScreen component.
     * @return the initialized component instance
     */
    public SplashScreen getSplashScreen() {
        if (splashScreen == null) {//GEN-END:|63-getter|0|63-preInit
            // write pre-init user code here
            splashScreen = new SplashScreen(getDisplay());//GEN-BEGIN:|63-getter|1|63-postInit
            splashScreen.setTitle("Click\'N\'Gage");
            splashScreen.setCommandListener(this);
            splashScreen.setImage(getLogo());
            splashScreen.setText("Welcome to Click\'N\'Gage!");//GEN-END:|63-getter|1|63-postInit
            // write post-init user code here
        }//GEN-BEGIN:|63-getter|2|
        return splashScreen;
    }
    //</editor-fold>//GEN-END:|63-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: logo ">//GEN-BEGIN:|67-getter|0|67-preInit
    /**
     * Returns an initiliazed instance of logo component.
     * @return the initialized component instance
     */
    public Image getLogo() {
        if (logo == null) {//GEN-END:|67-getter|0|67-preInit
            // write pre-init user code here
            try {//GEN-BEGIN:|67-getter|1|67-@java.io.IOException
                logo = Image.createImage("/gr/academic/city/msc/industrial/mobileclickers/mobilemodule/images/logo.png");
            } catch (java.io.IOException e) {//GEN-END:|67-getter|1|67-@java.io.IOException
                e.printStackTrace();
            }//GEN-LINE:|67-getter|2|67-postInit
            // write post-init user code here
        }//GEN-BEGIN:|67-getter|3|
        return logo;
    }
    //</editor-fold>//GEN-END:|67-getter|3|

    /**
     * Returns a display instance.
     * @return the display instance.
     */
    public Display getDisplay() {
        return Display.getDisplay(this);
    }

    /**
     * Exits MIDlet.
     */
    public void exitMIDlet() {
        switchDisplayable(null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called when MIDlet is started.
     * Checks whether the MIDlet have been already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp() {
        if (midletPaused) {
            resumeMIDlet();
        } else {
            initialize();
            startMIDlet();
        }
        midletPaused = false;
    }

    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp() {
        midletPaused = true;
    }

    /**
     * Called to signal the MIDlet to terminate.
     * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
    }

    public MobileClickersWSService getWebService() {
        if (webService == null) {
            webService = new MobileClickersWSService_Stub();
        }

        return webService;
    }

    private void getNumberOfAnswers() throws Exception {
        try {
            questionCode = getQuestionCodeTextField().getString();
            int numberOfAnswers = getWebService().getNumberOfAnswer(questionCode);

            if (numberOfAnswers == -1) {
                resetForms();
                throw new Exception();
            }

            char answerChar = 'A';

            for (int i = 0; i < numberOfAnswers; i++) {
                getAnswersList().append(String.valueOf(answerChar), null);
                answerChar++;
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    private void submitAnswer() throws Exception {
        try {
            String answer = getAnswersList().getString(getAnswersList().getSelectedIndex());

            String uniqueSubmissionCode = "";

            if (System.getProperty("phone.imei") != null) {
                uniqueSubmissionCode += System.getProperty("phone.imei");
            }
            if (System.getProperty("com.nokia.IMEI") != null) {
                uniqueSubmissionCode += System.getProperty("com.nokia.IMEI");
            }
            if (System.getProperty("com.sonyericsson.imei") != null) {
                uniqueSubmissionCode += System.getProperty("com.sonyericsson.imei");
            }
            if (System.getProperty("com.samsung.imei") != null) {
                uniqueSubmissionCode += System.getProperty("com.samsung.imei");
            }
            if (System.getProperty("com.siemens.imei") != null) {
                uniqueSubmissionCode += System.getProperty("com.siemens.imei");
            }
            if (System.getProperty("com.nokia.mid.imei") != null) {
                uniqueSubmissionCode += System.getProperty("com.nokia.mid.imei");
            }
            if (System.getProperty("com.siemens.IMEI") != null) {
                uniqueSubmissionCode += System.getProperty("com.siemens.IMEI");
            }
            if (System.getProperty("IMEI") != null) {
                uniqueSubmissionCode += System.getProperty("IMEI");
            }
            if (System.getProperty("com.motorola.IMEI") != null) {
                uniqueSubmissionCode += System.getProperty("com.motorola.IMEI");
            }

            int submitAnswer = getWebService().submitAnswer(questionCode, answer, uniqueSubmissionCode);
            resetForms();
            if (submitAnswer == -1) {
                throw new Exception();
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    private void resetForms() {
        getAnswersList().deleteAll();
        getQuestionCodeTextField().setString(null);
    }
}
