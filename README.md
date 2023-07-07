# WavesetTransformer: For Trevor Wishart


## In short...

This group of classes for SuperCollider constitute a fully playable instrument utilizing the [waveset](http://www.trevorwishart.co.uk/transformation.html) form of granular synthesis pioneered by [Trevor Wishart](https://en.wikipedia.org/wiki/Trevor_Wishart). The software includes all of the waveset transformations described by Wishart, as documented in [Curtis Road's](https://en.wikipedia.org/wiki/Curtis_Roads) [Microsound book](https://monoskop.org/images/d/d1/Roads_Curtis_Microsound.pdf), plus a few extra of my own discovery. A waveset, is a chunk of a waveform whose amplitude crosses zero on the x-axis. This description isn't of much importance to use of the instrument, other than to say, the instrument takes a waveform (an audio file) and breaks it into many small chunks of sound --grains, if the reader prefers-- and allows for the manipulation of each individual chunk. The instrument can be used for live performance, live playback, or as a tool for creating sounds to be edited together, that is, the instrument functions as any other instrument.

 Tutorial videos and some sounds made with the software can be found [here](https://drive.google.com/drive/folders/1iEzP5V_tBxfhUb5TMoJkKBgdawS8afj9?usp=share_link).

The help files for WavesetTransformerPlayer also explain how to use this instrument and what each transformation does. A help file can be accessed by placing a cursor over the class name and hitting `cmd d`, as seen below. The help files and tutorials do not go in depth, almost at all, into the nature of the transformations, as they are a bit more technical. Please consult Wishart or Road’s writing for more information on this subject.

<img width="506" alt="Screen Shot 2023-02-26 at 7 29 15 PM" src="https://user-images.githubusercontent.com/16430294/221447129-eaad0bf6-b151-44be-9b10-1c83c2e6ef77.png">

After reading this ReadME it is advised to look at [this repo](https://github.com/bkudler/waveset-starter-files) as it contains files that will quickly help a player get started with the instrument and use it with ease.

## Interface

There is no graphical user interface (some like to say GUI, pronounced “gooey”). Instead, the player interacts with this software through entering in values and executing lines of code using ScIDE, or another piece of software that can get sclang to execute and send messages from client to server. Not knowing what ScIDE is, or what the previous sentence means, won’t stop anyone from using this instrument. The only knowledge needed is that typing `shift` and `return` at the end of a line of text, or lines of text, in SuperCollider executes those lines of code, and, in this case, produces sound. 

New users of SuperCollider should complete up to step 05 in [this tutorial](https://doc.sccode.org/Tutorials/Getting-Started/00-Getting-Started-With-SC.html) before proceeding, it won’t take long.

  
<img width="977" alt="Screen Shot 2023-02-22 at 7 57 51 PM" src="https://user-images.githubusercontent.com/16430294/220798582-37c8d49f-f7b0-4f26-964d-9965e6fc1c5a.png">


<img width="965" alt="Screen Shot 2023-02-22 at 7 58 07 PM" src="https://user-images.githubusercontent.com/16430294/220798595-fc0739ae-2035-4093-a47c-41a5724f13ea.png">

<img width="972" alt="Screen Shot 2023-02-22 at 7 58 14 PM" src="https://user-images.githubusercontent.com/16430294/220798838-a0f9d6b5-59ae-49ca-b2ef-f5493c5d8362.png">


There is more behind the choice to not include a GUI than laziness or putting a barrier between the player and the software —though those were motivators. From a practical standpoint, there maybe actions a player wants to take that are physically difficult or impractical with a GUI; moving a series of knobs in different directions (some up, some down) while pushing buttons that are far away on the screen or whatever outboard controller the player has mapped the GUI to. Executing several disparate commands, as one would do by pushing buttons and moving knobs, in real time is as easy as executing a single command —`shift return`. There are, of course, pitfalls to this approach, such as having to group all of the commands together in the text editor before executing them. This tradeoff is a rather small. Another point of practical ease is being able to set the parameters to exact values, rather than having to approximate with sliders and knobs.

Riding the elevator a floor up in the tower of abstraction, there are social concerns the lack of GUI addresses. Interacting with a computer using a programming language is, largely, seen as esoteric, difficult, or requiring some level of deep intelligence. Conversely, there are those who try to cast this sort of interaction as easy, simple, and available to anyone with a small bit of willingness. This group creates software tools whose goals are only to prove the simplicity and ease of code. When the user of this software goes to work in a context with lighter guardrails, they find the truth to be quite other. 

The core of this interface is modifying, writing, and executing small pieces of code that will accomplish artistic goals for the player. Hopefully participating in this process, over and over, will show that programming language based computer interaction is approachable, but not simple, available to most (regardless of intelligence), and, foremost, useful.

One more level up on the elevator, to the final stop: aesthetic and philosophical concerns. The act of writing, here typing, is traditionally alienated from music creation, as its primary (near only) connection is producing a score that, is, later, translated into music. The score stands separate from the sounds, its distance magnificent to anyone gazing at the document-cum-score. Even in the case of aleatoric scores or scores that ask the players to improvise, the “piece of music” must be completed before a player can work with it. 

A text based interface brings a small bit of writing to live music creation. The way that the player interacts with an instrument, of course, informs the way the player perceives and plays the instrument. Similar to how the medium of an art work affects the way the consumer of the piece understands and appreciates the work (VINYL ONLY!). The methods used to create a piece of work affects how the artist crafts the work. What effects will writing, rather than GUI navigating, produce? The player’s gestures are not mapped to button pushes or knob twists that weakly imitate the gestures of more traditional instruments, rather the gestures are based in the quotidian, timeless, and incredibly enriching act of writing (even if it is just numbers and copy and pastes!).

## Installation

1. [Install SuperCollider](https://supercollider.github.io/)

2. [Install the SC3-plugins](https://github.com/supercollider/sc3-plugins)

    In reality the only file needed is [ProbabilityDistributions.sc](https://github.com/supercollider/sc3-plugins/blob/main/source/LoopBufUGens/sc/classes/LJP%20Classes/ProbabilityDistributions.sc). Add this file to the SuperCollider extensions folder, this can be found using the README for the SC3-extensions. The entire SC3-plugins folder is worth having and adds very little heft to SuperCollider. It is highly suggested to download the entire package, plus, it is easier for installation. For the reader who is unfamiliar with github, to download a file, click the green "Code" button in the top righthand corner.

3. Install this quark. There are lots of ways to install quarks. [This page](https://doc.sccode.org/Guides/UsingQuarks.html) lists some of them. The recommended way of installation is to run the following line of code:

    `Quarks.install("https://github.com/bkudler/WavesetTransformer-ForTrevorWishart")`.
    
    This will install the Waveset instrument. It may take a moment, be patient.

4. Recompile the class library.

    In the standard SuperCollider interface, in the bottom right corner, there is a monitor for the interpreter and the server. Click on the display that says "active", next to the interpreter, and select "Recompile Class Library".

    <img width="311" alt="Screen Shot 2023-02-22 at 7 59 45 PM" src="https://user-images.githubusercontent.com/16430294/220798724-1c7e012f-7736-4c1e-8069-3bd699f5466b.png">

## Getting Sound

Sample files to work with can be found [here](https://github.com/bkudler/waveset-starter-files), for the user who does not want to build the interface from scratch. Start with `boot-file.scd` to initialize a WavesetTransformerPlayer object, which all sound will generate from. Use  `starter.scd` to tell that object to start making sound. The tutorials folder also contains a video called “Getting-Sound” which explains how to get started with the instrument. The help files for the WavesetTransformerPlayer contain more information on how to use the instrument, including helpful values for transformations.

"sample-jam-two" in the linked google drive provides a sonic example of every transformation and the types of sounds this software leans towards making, though the breadth of sound is quite wide and cannot be contained in a single (or multiple) audio recordings.

## Using Transformations

The file labeled `transformers.scd` contains more advanced transformations, involving doing more than changing one quality of a waveset. The video labeled “Using-Transformations” and “Using-Transformations-II” shows how this file can be used. Again, the help file also contains information on how to use these transformations.

## Presets

The file `presets.scd` in the starter files package has some presets that can be examined using the `takeSnapshot` and `showSnapshot` methods, or, by just executing them and seeing how they sound.
  
## Advanced Usage

There is one advanced feature that doesn’t transform wavesets. A “Scout” class is built in with this quark, which calls a list of functions whenever certain things happen in the lifecycle of the waveset player. See the video “Advanced-Usage” for more information. The help file also has examples.

The events are 

1. wavesetEnd, triggered when a waveset finishes playing

2. wavesetGroupEnd, triggered when a chunk of wavesets finish playing, as done with the customPhrase function

3. rampEnds, triggered when a ramp function finishes.

4. wavesetLoopEnd, triggered when a full loop of all the wavesets in the current set is complete.

5. wavesetLoopsFinshed, triggered when all of the waveset loops are finished

A user doesn’t have to ever use this feature to get the full sonic capabilities described by Wishart and Roads.

## Issues

Please start an issue in the issues tab, should an issue arise.

or

Email bkudler@gmail.com.
