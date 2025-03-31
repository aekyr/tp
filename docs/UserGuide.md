---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# GuestNote User Guide

GuestNote is a **desktop app for hotel concierge, optimized for use via a  Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, GuestNote can get your guest management tasks done faster than traditional GUI apps.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).
<panel header="_Not sure which Java version you have?_" no-close>
<tabs>
    <tab header="Windows">
        To check your Java version on Windows, please follow these steps:
        <ol>
            <li>Open Command Prompt.</li>
            <li>Type `java -version` and press Enter.</li>
            <li>Check the version of Java installed on your computer:</li>
        </ol>
        <img src="images/ug/checkjavaversion_windows.jpeg" alt="Java version Windows" style="width:100%;">
    </tab>
    <tab header="Mac">
        To check your Java version on Mac, please follow these steps:
        <ol>
            <li>Open Terminal.</li>
            <li>Type `java -version` and press Enter.</li>
            <li>Check the version of Java installed on your computer:</li>
        </ol>
        <img src="images/ug/checkjavaversion_mac.png" alt="Java version Mac" style="width:100%;">
    </tab>
</tabs>
</panel>
<br>

2. Download the latest `.jar` file from [here](https://github.com/AY2425S2-CS2103T-W09-2/tp/releases).

3. Copy the file to the folder you want to use as the _home folder_ for your GuestNote.

4. Open a command terminal, and use the `cd` command to navigate into the folder you put the jar file in.<br>Run `java -jar guestnote.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
![firstview.png](images/ug/firstview.png)
5. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
<panel header="Take it for a spin, let's try out some commands!" no-close expanded>
    <tabs>
        <tab header="**Parts of the App**">
            <markdown>
**The app is divided into 3 parts:**
1. **Command Box:** This is where you type the commands.
2. **Output Box:** This is where the result of the command is shown.
3. **Guest Book:** This is where the list of guests is shown.
![labelledUI.png](images/ug/labelledUI.png)
            </markdown>
        </tab>
        <tab header="**Try out the help command**">
            <markdown>
**Type `help` and press Enter to see the help window.**
            </markdown>
            <panel header="Help! Where do I type **`help`**?" no-close>
                <markdown>
Type the command **`help`** into the Command Box, as shown below:
![command_input_help.png](images/ug/command_input_help.png)
                </markdown>
            </panel>
            <panel header="What you should see:" no-close expanded>
                <markdown>
Now you can always come back to this page if you need help!
![help_command_executed.png](images/ug/help_command_executed.png)
                </markdown>
            </panel>
        </tab>
        <tab header="**Try out some other commands**">
            <markdown>
Some example commands you can try:

* `list` : Lists all guests.

* `add n/John Doe p/98765432 e/johnd@example.com r/01-01 rq/One extra pillow` : <br>Adds a guest named `John Doe` who are in room 01-01 with a request of one extra pillow to the Guest list.

* `delete 3` : Deletes the 3rd guest shown in the current list.

* `clear` : Deletes all guests.

* `exit` : Exits the app.

* `help` : Shows the help window.

* `find John` : Finds all guests whose names contain the word `John`.

* `edit 2 n/James Lee`: Edit guest No. 2 to be named `James Lee`.

* `check-in 1` : Check in guest No.1
            </markdown>
        </tab>
    </tabs>
</panel>
<br>

6. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [rq/REQUEST]` can be used as `n/John Doe rq/New Pillow` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[rq/REQUEST]…​` can be used as ` ` (i.e. 0 times), `rq/New Pillow`, `rq/New Pillow rq/Orange Juice` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</box>

### Viewing help : `help`

Shows a message explaning how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a guest: `add`

Adds a guest to the guest list.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL [rq/REQUEST]…​`

<box type="tip" seamless>

**Tip:** A guest can have any number of requests (including 0)
</box>

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com r/01-01`
* `add n/Betsy Crowe rq/Add Pillow e/betsycrowe@example.com p/1234567 r/01-01 rq/Orange Juice`

### Listing all guests : `list`

Shows a list of all guests in the guest list.

Format: `list`

### Editing a guest : `edit`

Edits an existing guest in the guest list.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [rq/REQUEST]…​`

* Edits the guest at the specified `INDEX`. The index refers to the index number shown in the displayed guest list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing requests, the existing requests of the guest will be removed i.e adding of requests is not cumulative.
* You can remove all the guest’s requests by typing `rq/` without
    specifying any requests after it.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st guest to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower rq/` Edits the name of the 2nd guest to be `Betsy Crower` and clears all existing requests.

### Locating guests by name: `find`

Finds guests whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Guests matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Deleting a guest : `delete`

Deletes the specified guest from the guest list.

Format: `delete INDEX`

* Deletes the guest at the specified `INDEX`.
* The index refers to the index number shown in the displayed guest list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd guest in the guest list.
* `find Betsy` followed by `delete 1` deletes the 1st guest in the results of the `find` command.

### Clearing all entries : `clear`

Clears all entries from the guest list.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

GuestNote data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

GuestNote data are saved automatically as a JSON file `[JAR file location]/data/guestnote.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, GuestNote will discard all data and start with an empty data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the GuestNote to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

### Check-In a guest : `check-in`

Check-in a guest to the hotel.

Format: `check-in INDEX`

* Check-in the guest at the specified `INDEX`.
* If the guest is already checked-in, the command will not have any effect and an exception will show up in the app.

### Check-Out a guest : `check-out`

Check-out a guest from the hotel.

Format: `check-out INDEX`

* Check-out the guest at the specified `INDEX`.
* If the guest is already checked-out, the command will not have any effect and an exception will show up in the app.
* If the guest is not checked in yet, the command will not have any effect and an exception will show up in the app.

### Extended Find `[coming in v1.4]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous GuestNote home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action     | Format, Examples
-----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------
**Add**    | `add n/NAME p/PHONE_NUMBER e/EMAIL r/ROOM-NUMBER [rq/REQUEST]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com r/01-01 rq/Add Pillow rq/Orange Juice`
**Clear**  | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit**   | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [rq/REQUEST]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Find**   | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List**   | `list`
**Help**   | `help`
**Exit**   | `exit`
**Check-In** | `check-in INDEX`<br> e.g., `check-in 1`
**Check-Out** | `check-out INDEX`<br> e.g., `check-out 1`
