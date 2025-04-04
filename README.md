<h1>Church/Tombstone payment program</h1>
<br>
<p>This program was created to simplify the payments of the church taxes that every catholic in Kosovo is obliged to do for maintenance.</p>
<p>It was created using Java as the main programming language, JavaFX as the GUI and SQLite as the database to store the instances locally.</p>
<br>
<h2>How to use it</h2>
<h3>Registering a person</h3>

<h3>Home icon</h3>
<p>This home icon pops up a window in which YOU HAVE TO type your churches name in it since it will appear in the evidence which we can print.</p>

<p>You start the program with finding yourself at the start page in which you have 4 buttons to choose from, let's explain the Church tax payments one since it is interlinked with the Tombstone payments one.
You go in there and there is an empty table, you create an instance of a person using the add button on the left, a form pops up asking for some basic information which is crucial to the function of the program, all
of the column are obligatory excluding the phone number(there are cases where people don't want to add it), after that we create the instance and save it into the database using the save button provided on the form.
And there you go we have our first person who is registerd there in the program. There are other buttons provided which as you guessed do the things the name says about them.</p>
<br>

<h3>Registering a payment for a person</h3>
<p>After populating the table, let's show you how to add payments for that person, we double click his name on the table and we get sent to another panel with a table where we can add instances of payments.
Click the add button, it will pop up a form with the information of that specific person and some other stuff to add, you will need to add the year of the payment he is going to do(example he will pay the tax of 
2021 in the year 2025, so add the year 2021 in that text area), the payment he is willing to make(is in euro, example 200 or 200.41 also works in decimal, no need to add the euro symbol), payment status which
will tell the status of the payment(not fully paid, paid, released from the payment) and the date on which the payment is being made. After these just press the save button and it gets saved to the database.</p>

<h3>Printing the evidence for the person</h3>
<p>After the specific payments you registerd, you can select on not select any of them form the table and press the get evidence button, it will put all the chosen payments, data about that person and a notice
(there is a notice button not mentioned in the section before, so what it does it is that for every instance you press it and press the notice button, it will pop up a notice for that instance and save it, which you
can use to print it in the get evidence panel), to get the notice you just double click the year and if there is any it will pop up in the notice section, we can also edit it with the same logic just don't forget
to press the edit button after being done. We have two other buttons which are save(saves to .png or .jpg) or just print(which sends to windows print panel to print the page).</p>


<p>Same logic goes for the Tombstone maintenance button.</p>

<h3>Statistics</h3>
<p>After pressing this button it will bring you to a statistics panel in which you have some pie charts which give you a statistic on how many people made payments for the selected year which you can choose with
the provided combo box, it is important to mention that if there are not ANY PAYMENTS made then there is no reason to go to this panel since its just empty</p>

<h3>Import/export</h3>
<p>This window provides you with the ability to import or export people and transactions from the program. The export exports everything in a .csv format and the import imports from a csv format(beware of using them
since it will delete the previous database elements after importing the .csv files).</p>

