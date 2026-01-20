# Mobile Computing 521046A: Exercise 1
* **Author:** Christoph Gasche <christoph.gasche@student.oulu.fi>
* **GitHub Link:** <https://github.com/chgasche/MobileComputing-2026/tree/master/Homework1>

## Description of the task

* The task of this week was to create a first Android app using *Android Studio* with *Jetpack Compose* and *Kotlin*. 

* I strictly followed the [Android Compose Tutorial - Jetpack Compose](https://developer.android.com/develop/ui/compose/tutorial) provided in the homework. 

* My idea was to adapt the tutorial example to aviation such that a flight departures table for fictive Oulu airport is displayed in my app. 

## What I did?

* Once the IDE had started, I started a *new Project* with an *Empty Activity*. This created a bunch of files and displayed a "Hello World" message. The code generated differed a bit from the one in the tutorial (that used a simpler approach). I then went back to the code of the tutorial and deleted/uncommented the auto-generated code.

* Then, it was step-by-step following the tutorial and adding *Composables*. First, a `FlightCard` (representing one flight) was added. There, several `Row` and `Columns` were used to align the text fields. Also, the data class `Flight` was introduced:
```
data class Flight(val time: String, val flightnr: String, val dest: String, val status: String, val checkin: String, val gate: String, val timeSTA: String)
```

* Next, an image as resource was added. I wanted to add the logo of airlines (Finnair, SAS, etc.) to my app. These images had to be added by using the *Resource Manager* on the left hand. Then, the code from the tutorial was used. In addition, I introduced an if-conditional to switch the logo according to the flight number (e.g. `AY101` -> resource `ay` )
```
// Set airline logo
    val airlineLogo = if(flight.flightnr.contains("AY")) {
        R.drawable.ay
    } else if(flight.flightnr.contains("SK")) {
        R.drawable.sk
    } else {
        R.drawable.default_airline
    }
```

```
        Image(
            painter = painterResource(airlineLogo),
            contentDescription = "Airline Logo",
            modifier = Modifier
                // Set image size
                .size(30.dp)
                // Clip image to be shaped as a circle
                .clip(CircleShape)
                // Set border
                .border(0.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
        )
```

* A file `SampleData.tk` was created (see challenges below) to be used with `LazyList`

* In the end, a clickable functionality with expanded/collapsed context was added. For that, the variable `isExpanded` is declared:
	```
	// We keep track if the message is expanded or not in this
	    var isExpanded by remember { mutableStateOf(false) }
	```
	This variable is changed, when a *Column* is clicked:
	```
	Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) { ... }
	```


## Challenges

Some challenges I faced that went beyond the content of the tutorial:

- ***How to change text color to other colors?*** 
	The site <https://developer.android.com/develop/ui/compose/designsystems/material3> helped.


- ***Where to create the file `SampleData.kt`?***
	Via the plus sign in the Project view, I created a "Sample Data Directory" called `sampledata` where I placed the `SampleData.kt` file. There, I needed to specify the package:
	```
	package com.example.homework1application
	```

- ***How to extend `Surface` to full width?***
	This was achieved via using `modifier = Modifier.fillMaxWidth()`


- ***Chaining modifiers?***
	If several modifiers need to be specified, e.g. padding and animation, there exists a chaining syntax. See <https://developer.android.com/develop/ui/compose/layouts/constraints-modifiers>
		
- ***How to publish to GitHub?***
	For that, I followed the following tutorial on YouTube: <https://www.youtube.com/watch?v=GhfJTOu3_SE>
	

	There is *VCS* integration in *Android Studio*. Once a new GitHub repository has been created (I did this online on the website), the repo link can be added to *Android Studio*. Then a branch "*master*" can be pushed.
