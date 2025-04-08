---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# GuestNote Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**
- This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org/)
- The formatting of our UserGuide is inspired by UserGuide of group's from previous semesters: [(AY2425S1-CS2103T-F15-4)](https://ay2425s1-cs2103t-f15-4.github.io/tp/UserGuide.html) and [(AY2425S1-CS2103T-T09-1)](https://ay2425s1-cs2103t-t09-1.github.io/tp/UserGuide.html).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](https://github.com/AY2425S2-CS2103T-W09-2/tp/blob/master/docs/SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The * * *Architecture Diagram* * * given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2425S2-CS2103T-W09-2/tp/blob/master/src/main/java/seedu/guestnote/Main.java) and [`MainApp`](https://github.com/AY2425S2-CS2103T-W09-2/tp/blob/master/src/main/java/seedu/guestnote/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2425S2-CS2103T-W09-2/tp/blob/master/src/main/java/seedu/guestnote/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `GuestListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2425S2-CS2103T-W09-2/tp/blob/master/src/main/java/seedu/guestnote/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2425S2-CS2103T-W09-2/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Guest` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2425S2-CS2103T-W09-2/tp/blob/master/src/main/java/seedu/guestnote/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `GuestNoteParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a guest).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `GuestNoteParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `GuestNoteParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2425S2-CS2103T-W09-2/tp/blob/master/src/main/java/seedu/guestnote/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Guest` objects (which are contained in a `UniqueGuestList` object).
* stores the currently 'selected' `Guest` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Guest>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

### Storage component

**API** : [`Storage.java`](https://github.com/AY2425S2-CS2103T-W09-2/tp/blob/master/src/main/java/seedu/guestnote/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `GuestNoteStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.guestnote.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### List all Guests with Requests Feature
This feature is an extension of the standard list command. It allows users to view all guests and their details if the guest has at least one request. 

#### Implementation

Given below is an example usage scenario of how the listing all guests with requests occurs.

**Example Usage: `list rq`**

Step 1. The user executes `list rq`. The input is sent to the `ListCommandParser`.

Step 2. The `ListCommandParser` checks the input. Recognizing the parameter, the parser creates a new `ListCommand` with the request filter enabled (i.e., new ListCommand(true)).

Step 3. When `ListCommand` is executed, the model verifies the flag and calls `model.updateFilteredGuestList` to filter the guest list for guests with at least one request.

Step 4. The command then returns a `CommandResult` with the message `Listed all guests with requests`. This message is displayed to the user, and the UI updates to show only the guests with recorded requests.

### Find Feature
This feature intends to list the guests if any guest fields match with any search terms. A match occurs when at least one guest field contains a full-word, case-insensitive match for any one of the search terms. 

#### Implementation

Given below is an example usage scenario of how the find feature used in GuestNote.

**Example Usage: `find alex 23-32`**<br>
_In this case, the search terms are "alex" and "23-32"._

Step 1. The user executes `find alex 23-32`. The input is sent to the `FindCommandParser`.

Step 2. The parser trims the input and splits it into keywords (e.g. "alex" and "23-32"). It then creates multiple `FieldContainsKeywordsPredicate` objects—one for each guest field.

Step 3. The individual field predicates are combined into a single composite predicate. This composite predicate works as follows:<br>
• For a given guest, each individual field predicate is applied.<br>
• If at least one predicate returns true (i.e. a match occurs), the composite predicate returns true.

Step 4. The `FindCommand` is constructed and then returned to the `Logic` component for execution.

Step 5. When the command is executed, the command updates the filtered list to include only those guests who satisfy the search criteria.

Step 6. The command then returns a `CommandResult` with a message indicating how many guests have been found. The UI then updates to display the filtered guest list accordingly.

### Check-in Feature
This feature allows users to check in a guest into the hotel. 

#### Implementation

Given below is an example usage scenario of how the check-in feature used in GuestNote.

**Example Usage: `check-in 1`**

Step 1. The user executes `check-in 1`. 

Step 2. The Logic Manager receives the text input and passes it to `GuestNoteParser`. 

Step 3. `GuestNoteParser` creates a new` CheckInCommandParser` and calls its parse method, which extracts the guest index. 

Step 4. `CheckInCommandParser` creates a new `CheckInCommand` object, which is then passed back to Logic Manager for execution.

Step 5. When the command is executed, the command returns a `CommandResult` with a message indicating that the guest has been checked in. 

### Check-out Feature
This feature allows users to check a guest out of the hotel.

#### Implementation

Given below is an example usage scenario of how the check-out feature used in GuestNote.

**Example Usage: `check-out 2`**

Step 1. The user executes `check-out 2`.

Step 2. The Logic Manager receives the text input and passes it to `GuestNoteParser`.

Step 3. `GuestNoteParser` creates a new `CheckInCommandParser` and calls its parse method, which extracts the guest index. 

Step 4. `CheckInCommandParser` creates a new `CheckInCommand` object, which is then passed back to Logic Manager for execution.

Step 5. When the command is executed, `CheckOutCommand` calls `updateGuestStatus` to change the guest status to `CHECKED_OUT`. It returns a `CommandResult` with a message indicating that the guest has been checked out.

### Add Request Feature
This feature allows users to add requests made by guests. Requests can added along when the guest is originally created via the `AddCommand` or be added to an existing guest via the `EditCommand`.

#### Implementation

Given below is an example usage scenario of how a request is added alongside adding a new guest using `AddCommand`.

**Example Usage: `add n\John Doe p\98765432 e\johnd@example.com r\01-01 rq\One extra pillow`**

Step 1. The user executes `add n\John Doe p\98765432 e\johnd@example.com r\01-01 rq\One extra pillow`.

Step 2. The Logic Manager receives the text input and passes it to `GuestNoteParser`.

Step 3. `GuestNoteParser` creates a new `AddCommandParser` and calls its parse method, which extracts the guest name, phone, email, room, and request.

Step 4. `AddCommandParser` creates a new `AddCommand` object, which is then passed back to Logic Manager for execution.

Step 5. When the command is executed, the model calls `addGuest` to add the guest to the list of guests. It returns a `CommandResult` with a message indicating that the guest has been added.

Given below is an example usage scenario of how a request is added to an existing guest using `EditCommand`.

**Example Usage: `edit 2 +rq\One extra blanket`**

Step 1. The user executes `edit 2 +rq\One extra blanket`.

Step 2. The Logic Manager receives the text input and passes it to `GuestNoteParser`.

Step 3. `GuestNoteParser` creates a new `EditCommandParser` and calls its parse method, which extracts the guest index and the request. 

Step 4. `EditCommandParser` creates a new `EditCommand` object, which is then passed back to Logic Manager for execution.

Step 5. When the command is executed, the original guest and its details are retrieved. An `EditGuestDescriptor` object is created where the new request(s) are added to its `UniqueRequestList` field. `createEditedGuest` is then called with the modified `EditGuestDescriptor` object to create a new `Guest` object with updated guest fields. The model calls `setGuest` to update guest No.2 in the current list of guests. It returns a `CommandResult` with a message indicating that the guest has been edited.

### Delete Request Feature
This feature allows users to delete requests made by guests. Requests can be deleted using the `EditCommand` by passing in `-rq` prefix with the request or by passing in `-ri` prefix with the index of the request to be deleted. 

#### Implementation

Given below is an example usage scenario of how a request is deleted using `EditCommand` by passing in `-rq` prefix.

**Example Usage: `edit 2 -rq\One extra pillow`**

Step 1. The user executes `edit 2 -rq\One extra pillow`.

Step 2. The Logic Manager receives the text input and passes it to `GuestNoteParser`.

Step 3. `GuestNoteParser` creates a new `EditCommandParser` and calls its parse method, which extracts the guest index and the request. 

Step 4. `EditCommandParser` creates a new `EditCommand` object, which is then passed back to Logic Manager for execution.

Step 5. When the command is executed, the original guest and its details are retrieved. An `EditGuestDescriptor` object is created where the matching request is being removed from its `UniqueRequestList` field. `createEditedGuest` is then called with the `EditGuestDescriptor` to create a new `Guest` object with updated guest fields. The model calls `setGuest` to update guest No.2 in the current list of guests. It returns a `CommandResult` with a message indicating that the guest has been edited.

Given below is an example usage scenario of how a request is deleted using `EditCommand` by passing in `ri` prefix.

**Example Usage: `edit 2 -ri\1`**

Step 1. The user executes `edit 2 -ri\1`.

Step 2. The Logic Manager receives the text input and passes it to `GuestNoteParser`.

Step 3. `GuestNoteParser` creates a new `EditCommandParser` and calls its parse method, which extracts the guest index and request index.

Step 4. `EditCommandParser` creates a new `EditCommand` object, which is then passed back to Logic Manager for execution.

Step 5. When the command is executed, the original guest and its details are retrieved. An `EditGuestDescriptor` object is created where the request(s) at the matching indexes are removed. `createEditedGuest` is then called with the `EditGuestDescriptor` to create a new `Guest` object with updated guest fields. The model calls `setGuest` to update guest No.2 in the current list of guests. It returns a `CommandResult` with a message indicating that the guest has been edited.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:
* Has a need to manage a significant number of hotel guests
* Prefers desktop apps over other types e.g. webapp
* Can type fast to key in information
* Prefers typing to mouse interactions
* Is reasonably comfortable using CLI apps

**Value proposition**: manage hotel guests faster than a typical mouse/GUI driven app

---------------------------------------------------------

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                                                                                | So that I can…​                                                        |
|-------|--------------------------------------------|---------------------------------------------------------------------------------------------|------------------------------------------------------------------------|
| `* * *` | Hotel Concierge                            | Create guests' details (ie. Name, Phone, Email, and Room Number)                            | Add new guests to the hotel                                            |
| `* * *` | Hotel Concierge                            | Read guests' details                                                                        | Keep track of the information of hotel guests                          |
| `* * *` | Hotel Concierge                            | Update guests' details                                                                      | Edit guest information when a change is requested                      |
| `* * *` | Hotel Concierge                            | Delete guests and their details                                                             | Remove guests that request their information to be deleted             |
| `* * *` | Hotel Concierge                            | View a list of all guests                                                                   | Get a full overview of guests information to prioritize certain guests |
| `* * *` | Hotel Concierge                            | Search for guests using any single detail (ie. phone, email, name, room number, or request) | Quickly look up a specific guest's information                         |
| `* *` | Hotel Concierge                            | Create guests' requests                                                                     | Add requests made by guests                                            |
| `* *` | Hotel Concierge                            | Read guests' requests                                                                       | See what requests need to be completed                                 |
| `* *` | Hotel Concierge                            | Delete guests' requests                                                                     | Remove completed requests                                              |         
| `* *` | Hotel Concierge                            | Search for guest by request                                                                 | Match the request to the guest                                         |
| `* *` | Hotel Concierge                            | Update a guest's status from booking to check-in                                            | Avoid service errors to guests that have not arrived                   |
| `* *` | Hotel Concierge                            | Update a guest's status from check-in to check-out                                          | Avoid service errors to guests that have departed                      |
| `* *` | Hotel Concierge                            | View a guest's check-in/check-out status                                                    | Coordinate arrivals and departures                                     |
| `* *` | Newbie Hotel Concierge                    | Have easy access to a guide of commands                                                     | Easily navigate through the app and access necessary information       |
| `* *` | Newbie Hotel Concierge                    | See help text on command format                                                             | Understand the correct format for a command                            |
| `* *` | Experienced Hotel Concierge               | Enter commands using short-forms                                                            | Access required information with fewer keystrokes                      |
| `* *` | Experienced Hotel Concierge               | Quickly input most commonly used commands                                                   | Access and update information faster                                   |
| `* *` | Experienced Hotel Concierge               | Have power commands that shortcut multiple commands                                         | Streamline repetitive commands and workflows                           |
| `* *` | Concierge                                 | Pin guests                                                                                  | Quickly refer to guests without searching each time                    |
| `* *` | An impatient user                         | Experience reasonable response time                                                         | Use the app for large cases                                            |
| `* *` | A user who prefers hardcopy              | Export guest data in text format                                                            | Have a backup of guest details                                         |
| `* *` | Hotel Concierge                            | Log a guest's check-in date                                                                 | Maintain check-in records                                              |
| `* *` | Hotel Concierge                            | Log a guest's check-out date                                                                | Maintain check-out records                                             |
| `*`     | A potential user exploring the app       | See the app populated with sample data                                                      | Understand how the app will look in use                                |
| `*`     | A user ready to start using the app      | Purge all current data                                                                      | Remove sample/experimental data used for exploring the app             |

---------------------------------------------------------

### **Use Cases**

This document outlines the use cases for the GuestNote system, detailing the interactions between the system and the Hotel Concierge actor. Each use case includes a main success scenario (MSS) and possible extensions or variations.

Note for any given step in the following use case, we assume that they are atomic operations and are executed successfully unless otherwise specified in the extensions.

For all cases below, the **System** is the `GuestNote` and the **Actor** is the `Concierge`, unless specified otherwise.

---------------------------------------------------------

### Use case: UC01 - Create New Guest
<box type="info">
    <b>Preconditions:</b> Guest does not already exist in GuestNote.
</box>

**MSS:**
1. Concierge requests to create a new guest, passing the new guest’s details (ie. name, phone, email, room).
2. GuestNote validates the input and creates a new guest record.
3. GuestNote displays a success message confirming the guest’s creation.<br>
   Use case ends.

**Extensions:**
<box type="tip" header="1a. Optional Requests" light>
    <ul>
      <li>1a1. Concierge provides zero or more optional requests (e.g., special amenities, notes).</li>
      Use case resumes from step 2.</li>
    </ul>
</box>
<box type="wrong" header="1b. Invalid or Incomplete Details" light>
    <ul>
      <li>1b1. GuestNote detects that the provided information is missing compulsory fields or fails validation (e.g., invalid name or room number).</li>
      <li>1b2. GuestNote informs the Concierge of the error and prompts for corrections.
      <li>1b3. Concierge passes in the corrected details.<br>Use case resumes from step 2.</li>
    </ul>
</box>

---------------------------------------------------------

### Use case: UC02 - List Guests

**MSS:**
1. Concierge requests a list of all guests with their details (ie. name, phone, email, room, requests).
2. GuestNote retrieves and displays a list of all guests with their details. <br>
   Use case ends.

**Extensions:**
<box type="warning" header="1a. No Guests" light>
    <ul>
        <li>1a1. GuestNote detects that there are no guests in the system.</li>
        <li>1a2. GuestNote displays a message indicating that there are no guests.<br>Use case ends.</li>
    </ul>
</box>
<box type="tip" header="2a. Filter Guests to display those with Requests" light>
    <ul>
        <li>2a1. Concierge requests to see only guests with requests.</li>
        <li>2a2. GuestNote filters the list of guests based on them having requests.</li>
        <li>2a3. GuestNote displays the filtered list of guests.<br>Use case ends.</li>
    </ul>
</box>
<box type="tip" header="2b. Filter Guests by Name" light>
    <ul>    
        <li>2b1. Concierge provides a name to filter by.</li>
        <li>2b2. GuestNote filters the list of guests by comparing their names against the provided name.</li>
        <li>2b3. GuestNote displays the filtered list of guests.<br>Use case ends.</li>
    </ul>
</box>
<box type="tip" header="2c. Filter Guests by Search Term" light>
    <ul>    
        <li>2c1. Concierge provides a search term.</li>
        <li>2c2. GuestNote filters the list of guests by comparing all guest fields against that search term to find any matches.</li>
        <li>2c3. GuestNote displays the filtered list of guests.<br>Use case ends.</li>
    </ul>
</box>

---------------------------------------------------------

### Use case: UC03 - Update Guest Details
<box type="info">
<b>Preconditions:</b> Guest exists in GuestNote.
</box>

**MSS:**
1. Concierge retrieves a !!guest (UC02)!!
2. Concierge requests to edit the guest, passing the guest’s index in displayed list and new details (ie. name, phone, email, room).
3. GuestNote validates the input and updates the guest’s record.
4. GuestNote displays a success message confirming the update.  <br>
   Use case ends.

**Extensions:**
<box type="warning" header="2a. Guest Not Found" light>
    <ul>
      <li>2a1. GuestNote detects that the provided guest index does not exist in the system.</li>
      <li>2a2. GuestNote informs the Concierge that the guest was not found.<br>Use case ends.</li>
    </ul>
</box>
<box type="wrong" header="2b. Invalid or Incomplete Details" light>
    <ul>
      <li>2b1. GuestNote detects that required parameters is missing or the provided information fails validation (e.g., invalid name or room number).</li>
      <li>2b2. GuestNote informs the Concierge of the error and prompts for corrections.<br>Use case ends.</li>
      <li>2b3. Concierge passes in the corrected details.<br>Use case resumes from step 3.</li>
    </ul>
</box>

---------------------------------------------------------

### Use case: UC04 - Delete Guest
<box type="info">
    <b>Preconditions:</b> Guest exists in GuestNote.
</box>

**MSS:**
1. Concierge retrieves a !!guest (UC02)!!
2. Concierge requests to delete the guest, passing the guest’s index in displayed list.
3. GuestNote confirms the deletion of the guest.  <br>
   Use case ends.

**Extensions:**
<box type="wrong" header="2a. Guest Not Found" light>
    <ul>
      <li>2a1. GuestNote detects that the provided guest index does not exist in the system.</li>
      <li>2a2. GuestNote informs the Concierge that the guest was not found.<br>Use case ends.</li>
    </ul>
</box>
<box type="wrong" header="2a. Provided Index has Leading Zeros" light>
    <ul>
        <li>2a1. GuestNote detects that the provided index has leading zeros.</li>
        <li>2a2. GuestNote informs the Concierge of the error and prompts for corrections.</li>
        <li>2a3. Concierge passes in the corrected details.<br>Use case resumes from step 3.</li>
    </ul>
</box>
<box type="wrong" header="2a. Provided Index Is Too Large" light>
    <ul>
        <li>2a1. GuestNote detects that the provided index is larger than the system's maximum allowable value.</li>
        <li>2a2. GuestNote informs the Concierge of the error.<br>Use case ends.</li>
    </ul>
</box>

---------------------------------------------------------

### Use case: UC05 - Add New Request to Existing Guest
<box type="info">
<b>Preconditions:</b> Guest exists in GuestNote.
</box>

**MSS:**
1. Concierge retrieves a !!guest (UC02)!!
2. Concierge requests to edit the guest, passing the guest’s index in currently displayed list and the new request.
3. GuestNote validates the input and creates new a request record for the guest.
4. GuestNote displays a success message confirming the request creation.<br>
   Use case ends.

**Extensions:**
<box type="wrong" header="2a. Guest Not Found" light>
    <ul>
      <li>2a1. GuestNote detects that the provided guest index does not exist in the system.</li>
      <li>2a2. GuestNote informs the Concierge that the guest was not found.<br>Use case ends.</li>
    </ul>
</box>
<box type="wrong" header="3a. Invalid or Incomplete Details" light>
    <ul>
      <li>3a1. GuestNote detects that required parameters is missing or the provided information fails validation.</li>
      <li>3a2. GuestNote informs the Concierge of the error and prompts for corrections.</li>
      <li>2a3. Concierge passes in the corrected details.<br>Use case resumes from step 3.</li>
    </ul>
</box>

---------------------------------------------------------

### Use case: UC06 - Delete Request of Guest by Request Index
<box type="info">
    <b>Preconditions:</b>
    <ul>
        <li>Guest exists in GuestNote.</li>
        <li>Request of that index exists for the Guest.</li>
    </ul>
</box>

**MSS:**
1. Concierge retrieves a !!guest and the request index (UC02)!!
2. Concierge requests to delete the request for the guest, passing the guest’s index in currently displayed list and the index of the request to be deleted.
3. GuestNote confirms the deletion of the request. <br> 
   Use case ends.

**Extensions:**
<box type="wrong" header="2a. Guest Not Found" light>
    <ul>
        <li>2a1. GuestNote detects that the provided guest index does not exist in the system.</li>
        <li>2a2. GuestNote informs the Concierge that the guest was not found.<br>Use case ends.</li>
    </ul>
</box>
<box type="wrong" header="2b. Request Not Found" light>
    <ul>
      <li>2b1. GuestNote detects that the provided request index does not exist for the guest.</li>
      <li>2b2. GuestNote informs the Concierge that the request was not found.<br>Use case ends.</li>
    </ul>
</box>
<box type="wrong" header="2c. Invalid or Incomplete Details" light>
    <ul>    
        <li>2c1. GuestNote detects that required parameters is missing or the provided information fails validation.</li>
        <li>2c2. GuestNote informs the Concierge of the error and prompts for corrections.</li>
        <li>2c3. Concierge passes in the corrected details.<br>Use case resumes from step 3.</li>
    </ul>
</box>

---------------------------------------------------------

### Use case: UC07 - Check-In Guest
<box type="info">
    <b>Preconditions:</b>
    <ul>
        <li>Guest exists in GuestNote.</li>
        <li>Guest has not yet checked in.</li>
    </ul>
</box>

**MSS:**
1. Concierge retrieves a !!guest (UC02)!!
2. Concierge requests to check in the guest, passing the guest’s index in displayed list.
3. GuestNote confirms the guest has been checked in.<br>  
   Use case ends.

**Extensions:**
<box type="wrong" header="2a. Guest Not Found" light>
    <ul>
        <li>2a1. GuestNote detects that the provided guest index does not exist in the system.</li>
        <li>2a2. GuestNote informs the Concierge that the provided index was invalid.<br>Use case ends.</li>
    </ul>
</box>
<box type="wrong" header="2b. Guest Already Checked In" light>
    <ul>
        <li>2b1. GuestNote detects that the provided guest is already checked in.</li>
        <li>2b2. GuestNote informs the Concierge that the guest is already checked in.<br>Use case ends.</li>
    </ul>
</box>
<box type="wrong" header="2c. Guest Already Checked Out" light>
    <ul>
        <li>2c1. GuestNote detects that the provided guest is already checked out.</li>
        <li>2c2. GuestNote informs the Concierge that the guest is already checked out.<br>Use case ends.</li>
    </ul>
</box>

---------------------------------------------------------

### Use case: UC08 - Check Out Guest
<box type="info">
    <b>Preconditions:</b>
    <ul>
        <li>Guest exists in GuestNote.</li>
        <li>Guest has checked in but not yet checked out.</li>
    </ul>
</box>

**MSS:**
1. Concierge retrieves a !!guest (UC02)!!
2. Concierge requests to check out the guest, passing the guest’s index in displayed list.
3. GuestNote confirms the guest has been checked out.<br>  
   Use case ends.

**Extensions:**
<box type="wrong" header="2a. Guest Not Found" light>
    <ul>
        <li>2a1. GuestNote detects that the provided guest index does not exist in the system.</li>
        <li>2a2. GuestNote informs the Concierge that the provided guest index is invalid.<br>Use case ends.</li>
    </ul>
</box>
<box type="wrong" header="2b. Guest Not Checked In" light>
    <ul>
        <li>2b1. GuestNote detects that the provided guest has not checked in yet.</li>
        <li>2b2. GuestNote informs the Concierge that the guest has not checked in yet.<br>Use case ends.</li>
    </ul>
</box>
<box type="wrong" header="2c. Guest Has Checked Out" light>
    <ul>
        <li>2c1. GuestNote detects that the provided guest has already checked out.</li>
        <li>2c2. GuestNote informs the Concierge that the guest has already checked out.<br>Use case ends.</li>
    </ul>
</box>

--------------------------------------------------------------------------------------------------------------------
### **Non-Functional Requirements**
**Performance Requirements**
1. GuestNote should be able to hold up to 1000 persons without noticeable sluggishness in performance for typical usage.
2. GuestNote should respond within two seconds for most user operations.

**Usability Requirements**
1. GuestNote should only support a single user and does not support multiple users on a shared computer. 
2. GuestNote should target users who prefer keyboard-based interaction and are comfortable with fast typing, rather than relying on mouse-driven actions such as clicking buttons, selecting from dropdowns, or using drag-and-drop interfaces. 
3. GuestNote should target users who handle actions on an individual basis. All commands in GuestNote are designed for managing one guest at a time. 
4. GuestNote should target users who are meticulous and unlikely to make input errors. Actions such as check-in and check-out are designed to be irreversible, so the system assumes that users execute these commands with care and confidence.
5. GuestNote should target hotels with up to 99 floors and up to 99 rooms per floor. This supports a room numbering scheme in the format `FF-RR`, where:</br>
- `FF` represents the floor number (01 to 99), and</br>
- `RR` represents the room number on that floor (01 to 99)</br>
6. GuestNote should provide clear and user-friendly error messages when operations fail to assist users in correcting mistakes. 
7. GuestNote should be usable for resolutions of 1280x720 and higher, and for screen scales of 150%.

**Compatibility Requirements**
1. GuestNote should work on any mainstream OS as long as it has Java 17 or above installed.
2. GuestNote should work on Windows, Linux, and MacOS systems without relying on OS-dependent libraries or features.
3. GuestNote should not depend on a remote server. 

**Data Requirements**
1. GuestNote should not use an external database for data storage.
2. GuestNote should store data locally. 
3. GuestNote should be packaged into a single JAR file. 
4. GuestNote JAR file should not be above 100MB.

**Reliability Requirements**
1. The application should not crash under normal operations and should handle errors gracefully without data loss.
2. The application should maintain a stable performance over extended usage periods.

--------------------------------------------------------------------------------------------------------------------
### **Glossary**

| **Term**                              | **Definition / Example**                                                                                                                                                                                                             |
|---------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **AddressBook Level 3 (AB3)**         | The original open-source Java application from which GuestNote was adapted. AB3 serves as a contact management app with a CLI interface and basic CRUD functionality, forming the foundation for GuestNote’s structure and features. |
| **Backup**                            | A saved copy of your data file, used to restore the guest list in case of data loss.                                                                                                                                                 |
| **cd**                                | Stands for "change directory" – a terminal command used to navigate between folders.<br>e.g., `cd path/to/folder`                                                                                                                    |
| **Check-in**                          | The process of registering a guest’s arrival at the hotel.                                                                                                                                                                           |
| **Check-out**                         | The process of finalising a guest’s stay and removing their active record.                                                                                                                                                           |
| **CLI (Command Line Interface)**      | A text-based interface for interacting with software by typing commands, instead of using a graphical interface. Common in developer tools and command-based apps like GuestNote.                                                    |
| **Command**                           | An instruction typed into the CLI to perform an action in GuestNote.<br>e.g., `add`, `edit`, `check-in`                                                                                                                              |
| **CSV (Comma Separated Values)**      | A file format used for tabular data, where values are separated by commas and records by newlines. Can be opened by spreadsheet software like Excel.                                                                                 |
| **Guest**                             | A guest staying at the hotel whose information is stored in the GuestNote system.                                                                                                                                                    |
| **Guest Field**                       | A name, email, phone, room number, or request of a guest stored in the GuestNote system.                                                                                                                                             |
| **Guest Fields**                      | The name, email, phone, room number, and request(s) of a guest stored in the GuestNote system.                                                                                                                                       |
| **GuestNote**                         | The system responsible for managing hotel guest records, including personal details, requests, and check-in/check-out data.                                                                                                          |
| **GUI (Graphical User Interface)**    | A visual interface that allows users to interact with GuestNote using elements like buttons, lists, and panels.                                                                                                                      |
| **Home Folder**                       | The folder on your computer where GuestNote stores its data files.                                                                                                                                                                   |
| **Index**                             | A number representing the position of a guest in the list.<br>e.g., in `delete 2`, `2` is the index of the guest to be deleted.                                                                                                      |
| **JSON (JavaScript Object Notation)** | A lightweight data-interchange format that is easy to read and write. GuestNote uses JSON to store its internal data files.                                                                                                          |
| **JSON File Location**                | The path to the JSON data file used by GuestNote.<br>e.g., `/home/user/data/guestnote.json`                                                                                                                                          |
| **Mainstream OS**                     | Refers to common operating systems supported by GuestNote: **Windows**, **Linux**, **Unix**, and **MacOS**.                                                                                                                          |
| **Parameter**                         | A specific input provided with a command, usually in the form of a prefix and value.<br>e.g., `n/James` or `r/01-01`                                                                                                                 |
| **Request**                           | A service or action requested by a guest (e.g., room service, maintenance, additional amenities).                                                                                                                                    |
| **Status**                            | Indicates the guest's booking stage:<br>**BOOKED**, **CHECKED-IN**, or **CHECKED-OUT**                                                                                                                                               |
| **Search Term**                       | The white-space separated value used in the find feature e.g. for `find pillows`, it is `pillows`                                                                                                                                    |
| **UI (User Interface)**               | The overall layout and design of how users interact with GuestNote, including both CLI and GUI elements.                                                                                                                             |

---------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

Ensure that you have read the notes about command format and the various commands themselves from the [User Guide](https://ay2425s2-cs2103t-w09-2.github.io/tp/UserGuide.html), as they will not be repeated here. 

</box>

### Launch and shutdown

**Initial launch**
1. Download the [jar](https://github.com/AY2425S2-CS2103T-W09-2/tp/releases) file and copy into an empty folder

2. Double-click the jar file  
**Expected**: Shows the GUI with a set of sample contacts. The window size may not be optimum.

**Saving window preferences**
1. Resize the window to an optimum size. Move the window to a different location. Close the window.
2. Re-launch the app by double-clicking the jar file.<br>
**Expected**: The most recent window size and location is retained.

### **Adding a Guest**

Adding a guest while in the main guest list view.

**Prerequisites**
1. Guest list is visible using the `list` command. 
2. Guest to be added has a unique email and is not currently in guest list.

**Test Cases**
1. `add n\John Doe p\98765432 e\johnd@example.com r\01-01 rq\One extra pillow`  
   **Expected**: A new guest named `John Doe` is added to the list. All details (name, phone, email, room, request) are shown. Status of guest is `BOOKED`. A success message appears in the status bar.

2. `add n\Jane Doe e\janed@example.com r\01-01 rq\One extra pillow`  
   **Expected**: Guest `Jane Doe` is added. Phone is shown as `Not added`. Status of guest is `BOOKED`. A success message appears in the status bar.

3. `add n\June Doe p\98764444 e\juned@example.com r\01-01`  
   **Expected**: Guest `June Doe` is added without any requests. Status of guest is `BOOKED`. No requests are visible. A success message appears in the status bar.

4. Other Incorrect add commands to try:  
- `add n\ p\98765432 e\johnd@example.com r\01-01`  
- `add n\John Doe r\01-01`  
- `add`<br>
**Expected**: No guest is added. Error message is shown in the status bar, stating that an invalid add command was used and the correct add command format to follow.

### Deleting a guest

Deleting a guest while in the main guest list view.

**Prerequisites**
1. Guest list is visible using the `list` command.
2. Guest to be deleted exists in the guest list.

**Test Cases**
1. `delete 1`  </br>
   **Expected**: First contact is deleted from the list. Details of the deleted contact shown in the status message.

2. `delete 0`  </br>
   **Expected**: No guest is deleted. Error details shown in the status message, stating that index is not a non-zero unsigned integer. Status bar remains the same.

3. `delete 0001`</br>
   **Expected**: No guest is deleted. Error details shown in the status message, stating that leading zeros are not allowed. Status bar remains the same.

4. `delete`</br>
   **Expected**: No guest is deleted. Error details shown in the status message, stating that missing field guest index. Status bar remains the same.

5. `delete x` where x is greater than 2147483647</br>
   **Expected**: No guest is deleted. Error details shown in the status message, stating that large positive indexes are not allowed. Status bar remains the same.

6. `delete x` where x is larger than the size of the list and smaller than or equal to 2147483647</br>
   **Expected**: No guest is deleted. Error details shown in the status message, stating that provided guest index is invalid. Status bar remains the same.

### Editing a guest

Editing a guest while all guests are being shown.

**Prerequisites**
1. List all guests using the `list` command. 
2. Multiple guests in the list.

**Test Cases**
1. `edit 1 n\Jonathan Doe`  
**Expected**: Name of the first guest is updated to `Jonathan Doe`. Other details remain unchanged. Status message reflects successful edit.

2. `edit 1 +rq\Fruit Basket`  
**Expected**: Adds `Fruit Basket` to the list of requests for the first guest. Other details remain unchanged. Status message reflects successful edit.

3. `edit 1 -rq\Fruit Basket`  
**Expected**: Removes `Fruit Basket` from the request list of the first guest. Other details remain unchanged. If request is not found, error message is shown. Otherwise, status message reflects successful update.

4. `edit 1 n\ r\`  
**Expected**: No changes made. Compulsory parameters cannot be empty. Error message is shown in the status bar, stating that an invalid edit command was used and the correct edit command format to follow.

5. `edit 1 -ri\2 -ri\3`  
**Expected**: No changes made. Error message shown indicating that duplicate request removal prefixes are not allowed in the same command.

6. Other incorrect edit commands to try:  
- `edit`
- `edit x`
- `edit 5 abc`
- `edit 0`  
**Expected**: No guest is edited. Error message is shown in the status bar. 

### Listing

Listing all or filtering guests. Guests can be filtered to show only guests with names that match the provided name, or to show only guests with requests. 

**Test cases**
1. `list`  
**Expected**: All current guests in the system are shown in the guest list. Deleted guests are not shown. Status message reflects the success of the `list` command.

2. `list alex`  
**Expected**: Guests with names containing `alex` (e.g., `Alex Yeoh`, `Alexander Tan`) are shown. If no such guests exist, the guest list appears empty. Status message reflects the success of the `list` command.

3. `list xyz` where `xyz` does not match the names of any guest in the guest list
**Expected**: No guests are shown. The guest list is empty. Status message reflects the success of the `list` command.

4. `list rq\` when there exists guests with requests in the guest list
   **Expected**: Guests with requests are shown. Status message reflects the success of the `list` command.

5. `list rq\` when there are no guests with requests in the guest list
   **Expected**: An empty guest list is shown. Status message reflects the success of the `list` command.

### Finding a guest

Filtering guests by one or more guest fields.

**Prerequisites**
1. Guest list is populated with a variety of guests. 
2. Each test case assumes no guests have been deleted. 
3. All fields (e.g. phone, email, request) contain varied values to verify the scope of the `find` command.

**Test cases**
1. `find Alex`  
   **Expected**: Guests with names containing `Alex` (e.g., `Alex Yeoh`, `Alexis Tan`) are shown in the list. Matching is case-insensitive and includes partial keywords. Status message reflects the success of the `find` command.

2. `find Ander`  
   **Expected**: Guests with names containing `Ander` (e.g., `Ander Yeoh`) are shown in the list. Matching is case-insensitive and includes partial keywords. Status message reflects the success of the `find` command.

3. `find 9876`  
   **Expected**: Guests with phones that contain `9876` are displayed (e.g., `98765432`, `99898768`). Status message reflects the success of the `find` command.

4. `find 01-01`  
   **Expected**: Guests with room numbers matching `01-01` are shown. Partial matches (e.g., searching `01`) may also display other rooms like `01-02` or `02-01`). Status message reflects the success of the `find` command.

5. `find bed`  
   **Expected**: Guests with tags or requests that include the word `bed` (e.g., `bedside table`, `bed lamp`) are shown. Status message reflects the success of the `find` command.

6. `find`  
   **Expected**: No input keyword provided. Error message appears, stating that the find command provided is invalid and the correct find command format. Guest list remains unchanged.

7. `find xyz`  where `xyz` is does not match any field of any guest in the list
    **Expected**: No guests match the keyword `xyznotfound`. Guest list is empty. Status message reflects the success of the `find` command.

### Check In

Checking in a guest.

**Prerequisites**
1. The guest to be checked has not already checked-in (ie. must have status set to `BOOKED`).

**Test cases**
1. `check-in 3`  when the guest list is at least of length 3 and the third guest has status `BOOKED`.</br>
   **Expected**: Third guest in the current list of guests will have its status changed to `CHECKED-IN`.

2. `check-in 3`  when the guest list is at least of length 3 and the third guest has status `CHECKED-IN` or `CHECKED-OUT`.</br>
   **Expected**: An error is displayed, stating that the chosen guest has already been checked in.

3. `check-in x` when the guest list is shorter than length x.</br>
    **Expected**: An error is displayed, stating that the stated guest index is invalid.

4. `check-in`</br>
    **Expected**: No input guest index provided. Error message appears, stating that the provided check-in command is invalid and the correct check-in command format.

### Check Out

Checking out a guest.

**Prerequisites**
1. The guest to be checked out is already checked in and has not checked out (ie. must have status set to `CHECKED-IN`).

**Test cases**
1. `check-out 3`  when the guest list is at least of length 3 and the third guest has status `CHECKED-IN`.</br>
   **Expected**: Third guest in the current list of guests will have its status changed to `CHECKED-OUT`.

2. `check-out 3`  when the guest list is at least of length 3 and the third guest has status `BOOKED`.</br>
   **Expected**: An error is displayed, stating that the chosen guest has not checked in.

3. `check-out 3`  when the guest list is at least of length 3 and the third guest has status `CHECKED-OUT`.</br>
   **Expected**: An error is displayed, stating that the chosen guest already checked out.

4. `check-out x` when guest list is shorter than length x.</br>
   **Expected**: An error is displayed, stating that the stated guest index is invalid.

5. `check-out`</br>
   **Expected**: No input guest index provided. Error message appears, stating that the provided check-out command is invalid and the correct check-out command format.

### Saving data

**Dealing with missing/corrupted data files**
1. If upon running GuestNote, the guest's name/room number/status/email/contact number/request list is not as expected, open the `data` folder where GuestNote stores its data. 
2. In the `data` folder, open `guestnote.json` and identify the mistakes in the stored data. If the error is missing data, you can correct it by altering the data in `guestnote.json`.
3. Otherwise, the terminal from where `GuestNote.jar` is launched will log where the file is corrupted.
4. If the data is beyond repair, delete the entire data folder or the `guestnote.json` file to start afresh with sample data.

---------------------------------------------------------

## **Appendix: Planned Enhancements**

Team size: 5

1. **Separate Find Feature for each Guest Field**</br>
   Currently, the find feature works to search through all guest fields (ie. name, email, phone, room number, and request). This enhancement will allow a more specific search toward targeted needs. 
2. **Remove Phone Number After Adding**</br>
   Currently, users are unable to remove a phone number using the edit feature after adding their phone number using the add feature. This enhancement will give users more flexibility in managing phone. 
3. **Stronger Phone Number Validation**</br>
   Currently, phone numbers are not validated against country-specific formats. This enhancement introduces stricter validation by checking the presence and correctness of the country code, and whether the remaining number conforms to the valid length and format for that country. 
4. **Stronger Email Validation**</br>
   Currently, email addresses are not fully validated according to the standards defined in [RFC5322](https://datatracker.ietf.org/doc/html/rfc5322). This enhancement improves email validation by enforcing stricter checks to follow the RFC5322 format, such as (but not limited to):</br>
- Valid Top-Level Domain (TLD): Ensures the domain ends with a known, valid TLD (e.g. `.com`, `.org`).</br>
- MX Record Check: Confirms that the domain has mail exchange (MX) records and can receive emails.</br>
- Unicode and International Email Support: Supports internationalised email addresses with non-ASCII characters.</br>
- Blocked Reserved or Invalid Addresses: Filters out test or reserved domains (e.g. `example@invalid.com`, `@localhost`).
5. **Specific Error Messages for Missing Compulsory Fields**</br>
   Currently, when the user fails to add compulsory fields, GuestNote displays a generic error message stating invalid command format. This enhancement improves usability by providing more informative error messages that explicitly state which compulsory fields are missing. This helps users identify and correct their mistakes more easily.
6. **Restrict Duplicate Email and Phone Numbers Based on Family or Booking**</br>
   Currently, users can be added or edited to have the same email and/or same phone number as another existing guest in GuestNote. This enhancement will add further restrictions whereby only guests who are under the same family or the same booking will be able to share phone numbers and/or emails.
7. **Restrict Number of Guests in the Same Room**</br>
   Currently, multiple users can be added to the same room. This enhancement will add further restrictions on the number of guests that can be assigned to any given room, ensuring that room capacity limits are enforced.
