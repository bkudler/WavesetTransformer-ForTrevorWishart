WavesetTransformerPlayer {
	var <>waveset, <>currFunc, <>holderCurrSet, <>holderLeaveOneSet;
	var <>holderLeaveTwoSet, <>holderTransferSetOne, <>holderTransferSetTwo;
	var <>breakBottom = 9, <>breakTop = 20;
	var <>snapShot;
	var <>contBreak;
	var <>shouldScrambleSeries = false, <>shouldPlaySeries = false;
	var <>contMultiplierRandomBreak = false, <>contMultiplierDirectBreak = false, <>contSpeedRandomBreak = false, <>contSpeedDirectBreak = false, <>shouldPlayMode = false, <>shouldSwitchMode = false;

	*new {| subBufNum, files, wsOut, mainOut  |
        ^super.new.init(subBufNum, files, wsOut, mainOut);
    }

	init {| subBufNum, files, wsOut, mainOut  |
		this.waveset = WavesetTransformer(subBufNum, files, wsOut, mainOut);
		this.waveset.randomWave();
		this.waveset.subBuf.plot();
		this.snapShot = ();
		this.contBreak = {false};
    }

	go{arg amt;
		this.ensound(1,1);
		^this.waveset.go(amt)
	}

	ensound{arg  amt, add;
		this.waveset.setAmpAmt(amt);
		this.waveset.setAmpAdd(add);
	}

	plotSub{
		this.waveset.subBuf.plot();
	}

	randomWave{
		this.waveset.randomWave();
	}

	silence{
		this.ensound(0,0);
	}

	norm{
		this.currFunc = "norm";
		this.waveset.shouldAverage = false;
		this.waveset.waveSetAmount = {1};
		this.waveset.multiplier = {1};
		this.waveset.repeats = {1};
		this.waveset.baseSpeed = {1};
		this.waveset.resetInst();
		this.waveset.distance = 0;
		this.waveset.speedChangeModulo = this.waveset.currSet.xings.size*2;
		this.waveset.speedMod = 0;
		this.waveset.plusAmount = 0;
		this.waveset.shouldAverage = false;
		this.waveset.shouldSwap = false;
		this.waveset.shouldShuffle = false;
		this.waveset.shouldDelete = false;
		this.waveset.shouldDeletePause = false;
		this.waveset.shouldRandomDelete = false;
		this.waveset.shouldSub = false;
		this.waveset.shouldReverse = false;
		this.waveset.pan = 0;
		this.waveset.shouldPan = false;
		this.waveset.decBreak = false;
		this.waveset.shouldHarmonize = false;
		this.waveset.shouldTransferOne = false;
		this.waveset.shouldTransferTwo = false;
		this.waveset.shouldInterleaveOne = false;
		this.waveset.shouldInterleaveTwo = false;
		this.waveset.shouldNormalize = false;
		this.waveset.speedSwapModulo = this.waveset.currSet.xings.size*2;
		this.waveset.waveSubMod = this.waveset.currSet.xings.size*2;
		this.waveset.shouldShrink = false;
		this.waveset.shouldRepeatSwap = false;
		this.waveset.shouldMultiLongSwap = false;
		this.waveset.shouldLongSwap = false;
	}

	climb{
		this.norm();
		this.waveset.decBreak = false;
		this.waveset.shouldInterleaveTwo = true;
		this.waveset.interleaveTwoLevel = 2;
		this.waveset.interleaveTwoNormLevel = 2;
		this.waveset.shouldPan = true;
		this.waveset.panNormLevel = 60;
		this.waveset.panLevel = 60;
		this.waveset.pan = 1;
		this.waveset.shouldSwap = true;
		this.waveset.multiplier = {7.25};
		this.waveset.shouldRandomDelete = true;
		this.waveset.deleteReceiver = 13;
		this.waveset.deleteDeviation = 4;
	}

	humm{arg multiplier = 0.8, plusAmount = 0.09, harmLevel = 0.15;
		this.norm();
		this.currFunc = "humm";
		this.waveset.multiplier = {multiplier};
		this.waveset.baseSpeed = {7};
		this.waveset.plusAmount = plusAmount;
		this.waveset.waveSetAmount = {100};
		this.waveset.shouldHarmonize = true;
		this.waveset.harmLevel = harmLevel;
		this.waveset.distance = 0;
	}

	rev{
		this.norm();
		this.currFunc = "rev";
		this.waveset.shouldHarmonize = true;
		this.waveset.harmLevel = 0.9;
		this.waveset.plusAmount = 0.0;
		this.waveset.baseSpeed = {0.3};
		this.waveset.multiplier = 4;
		this.waveset.waveSetAmount = {4};
		this.waveset.distance = 0;
	}

	blow{
		this.norm();
		this.currFunc = "blow";
		this.waveset.multiplier = {1.25};
		this.waveset.baseSpeed = {8};
		this.waveset.waveSetAmount = {3};
		this.waveset.shouldHarmonize = false;
		this.waveset.distance = 0.0;
		this.waveset.plusAmount = 0.05;
	}

	slurp{arg multiplier = 3;
		this.norm();
		this.currFunc = "slurp";
		this.waveset.multiplier = {multiplier};
		this.waveset.baseSpeed = {8};
		this.waveset.waveSetAmount = {10};
		this.waveset.shouldHarmonize = true;
		this.waveset.harmLevel = 0.1;
		this.waveset.distance = 0;
		this.waveset.plusAmount = 0.2;
	}

	vasilate{
		this.norm();
		this.currFunc = "vasilate";
		this.waveset.multiplier = {0.4};
		this.waveset.baseSpeed = {5};
		this.waveset.waveSetAmount = {70};
		this.waveset.shouldHarmonize = true;
		this.waveset.harmLevel = 0.1;
		this.waveset.distance = 0;
		this.waveset.plusAmount = 0.0;
	}

	sayWow{arg baseSpeed = 3, multiplier = 0.5;
		this.norm();
		this.currFunc = "sayWow";
		this.waveset.multiplier = {multiplier};
		this.waveset.baseSpeed = {baseSpeed};
		this.waveset.waveSetAmount = {50};
		this.waveset.shouldHarmonize = false;
		this.waveset.distance = 0;
	}

	biter{arg multiplier = 8, waveSetAmount = 10;
		this.norm();
		this.currFunc = "biter";
		this.waveset.multiplier = {multiplier};
		this.waveset.baseSpeed = {0.3};
		this.waveset.waveSetAmount = {waveSetAmount};
		this.waveset.shouldHarmonize = true;
		this.waveset.harmLevel = 0.15;
		this.waveset.distance = 0.0;
	}

	biterSub{arg multiplier = 8, waveSetAmount = 10;
		this.norm();
		this.currFunc = "biterSub";
		this.waveset.multiplier = {multiplier};
		this.waveset.baseSpeed = {0.3};
		this.waveset.waveSetAmount = {waveSetAmount};
		this.waveset.shouldHarmonize = true;
		this.waveset.harmLevel = 0.15;
		this.waveset.distance = 0.0;
		this.waveset.subNormLevel = 0;
		this.waveset.shouldSub = true;
	}


	crankThatSoulja{
		this.norm();
		this.currFunc = "crankThatSoulja";
		this.waveset.shouldSwap = false;
		this.waveset.shouldReverse = false;
		this.waveset.distance = 0.025;
		this.waveset.multiplier = {2.20};
		this.waveset.baseSpeed = {2.20};
		this.waveset.plusAmount = 0.02;
		this.waveset.waveSetAmount = {17};
		this.waveset.shouldHarmonize = false;
	}

	wrinkle{
		this.norm();
		this.crankThatSoulja.value();
		this.currFunc = "wrinkel-crank";
		this.waveset.baseSpeed = {rrand(0.4,4.8)};
		this.waveset.plusAmount = 0.1;
	}

	sputter{
		this.norm();
		this.crankThatSoulja.value();
		this.currFunc = "sputter-crank";
		this.waveset.plusAmount = 0.02;
		this.waveset.baseSpeed = {rrand(3.2,5.8)};
	}

	drone{
		this.norm();
		this.currFunc = "drone";
		this.waveset.baseSpeed = {10};
		this.waveset.shouldHarmonize = false;
		this.waveset.distance = 0;
		this.waveset.multiplier = {40};
		this.waveset.shouldSwap = true;
		this.waveset.shouldReverse = false;
		this.waveset.waveSetAmount = {5};
		this.waveset.plusAmount = 0.2;
	}

	cook{arg multiplier = 8;
		this.norm();
		this.currFunc = "cook";
		this.waveset.baseSpeed = {0.435};
		this.waveset.distance = 0.0003;
		this.waveset.multiplier = {multiplier};
		this.waveset.shouldSwap = true;
		this.waveset.shouldReverse = true;
		this.waveset.waveSetAmount = {3};
		this.waveset.plusAmount = 0.005;
	}

	funWithNorm{
		this.norm();
		this.waveset.waveSetAmount = {20};
		this.waveset.distance = 0.0015;
		this.waveset.baseSpeed = 0.8;
	}

	freak{
		this.norm();
		this.currFunc = "freak";
		this.waveset.waveSetAmount = {30};
		this.waveset.baseSpeed = {4};
		this.waveset.multiplier = {2.21};
		this.waveset.shouldSwap = true;
		this.waveset.shouldHarmonize = false;
		this.waveset.plusAmount = 0.02;
		this.waveset.modulateSpeed.value();
		this.waveset.speed2 = this.waveset.baseSpeed*3;
	}

	bleepAbout{
		this.norm();
		this.currFunc = "bleepAbout";
		this.waveset.shouldSub = true;
		this.waveset.waveSubMod = 80;
	}

	subZipper{
		this.norm();
		this.currFunc = "subZipper";
		this.waveset.shouldSub = true;
		this.waveset.subLevel = 8;
		this.waveset.subNormLevel = 5;
		this.waveset.baseSpeed = {1.5};
		this.waveset.multiplier = {5};
		this.waveset.waveSetAmount = {10}
	}

	subAndBlop{
		this.norm();
		this.currFunc = "subAndBlop";
		this.waveset.shouldSub = true;
		this.waveset.subLevel = 12;
		this.waveset.subNormLevel = 12;
		this.waveset.baseSpeed = {2.1};
		this.waveset.multiplier = {5.35};
		this.waveset.waveSetAmount = {10};
		this.waveset.shouldSub = true;
		this.waveset.waveSubMod = 40;
		this.waveset.shouldSwap = true;
	}

	transferBlang{
		this.norm();
		this.currFunc = "transferBlang";
		this.waveset.shouldTransferTwo = true;
		this.waveset.transferTwoLevel = 2;
		this.waveset.transferTwoNormLevel = 2;
		this.waveset.speedSwapModulo = 3;
		this.waveset.speedSwapAmt = 0.7;
		this.waveset.decBreak = true;
		this.waveset.baseSpeed = {0.8};
		this.waveset.multiplier = {3};
		this.waveset.breakAmount = {rrand(0.001, 0.03)};
		this.breakBottom = 20;
		this.breakTop = 80;
		this.contBreak = {true};
		this.breakSwap(0.1);
	}


	moduloSpeedChange{arg modulo, modAmt, plusAmt;
		this.waveset.speedChangeModulo = modulo;
		this.waveset.speedMod = modAmt;
		this.waveset.plusAmount = plusAmt;
	}

	speedSwap{arg modulo, amt;
		this.waveset.speedSwapModulo = modulo;
		this.waveset.speedSwapAmt = amt;
	}

	longSwap{arg longSwapLevel, norm, speedAmt;
		this.waveset.shouldLongSwap = true;
		this.waveset.longSwapLevel = longSwapLevel;
		this.waveset.longSwapNormLevel = norm;
		this.waveset.speedSwapAmt = speedAmt;
	}

	multiLongSwap{arg multiLongSwapLevel, norm, multiAmt;
		this.waveset.shouldMultiLongSwap = true;
		this.waveset.multiLongSwapLevel = multiLongSwapLevel;
		this.waveset.multiLongSwapNormLevel = norm;
		this.waveset.multiSwapAmt = multiAmt;
	}

	repeatLongSwap{arg repeatLongSwapLevel, norm, repeatAdd;
		this.waveset.shouldRepeatSwap = true;
		this.waveset.repeatSwapLevel = repeatLongSwapLevel;
		this.waveset.repeatSwapNormLevel = norm;
		this.waveset.repeatSwapLev = repeatAdd;
	}


	shuffleSets{arg amt;
		this.waveset.shouldShuffle = true;
		this.waveset.shuffAmount = amt;
	}

	deleteSets{arg del, norm, pause;
		this.waveset.shouldDelete = true;
		this.waveset.deleteLevel = del;
		this.waveset.deleteNormLevel = norm;
		this.waveset.shouldDeletePause = pause;
	}

	randomDeleteSets{arg rec, dev, pause;
		this.waveset.shouldRandomDelete = true;
		this.waveset.deleteReceiver = rec;
		this.waveset.deleteDeviation = dev;
		this.waveset.shouldDeletePause = pause;
	}

	subSets{arg sub, norm;
		this.waveset.shouldSub = true;
		this.waveset.subLevel = sub;
		this.waveset.subNormLevel = norm;
	}

	reverseSets{arg rev, norm;
		this.waveset.shouldReverse = true;
		this.waveset.reverseLevel = rev;
		this.waveset.reverseNormLevel = norm;
	}

	harmonizeSets{arg harmLevel, harmAmt, normAmt;
		this.waveset.shouldHarmonize = true;
		this.waveset.harmLevel = harmLevel;
		this.waveset.harmonizeLevel = harmAmt;
		this.waveset.harmonizeNormLevel = normAmt;
	}

	transferSetsOne{arg transfer, norm;
		this.waveset.shouldTransferOne = true;
		this.waveset.transferOneLevel = transfer;
		this.waveset.transferOneNormLevel = norm;
	}

	transferSetsTwo{arg transfer, norm;
		this.waveset.shouldTransferTwo = true;
		this.waveset.transferTwoLevel = transfer;
		this.waveset.transferTwoNormLevel = norm;
	}

	interleaveSetsOne{arg inter, norm;
		this.waveset.shouldInterleaveOne = true;
		this.waveset.interleaveOneLevel = inter;
		this.waveset.interleaveOneNormLevel = norm;
	}

	interleaveSetsTwo{arg inter, norm;
		this.waveset.shouldInterleaveTwo = true;
		this.waveset.interleaveTwoLevel = inter;
		this.waveset.interleaveTwoNormLevel = norm;
	}

	normalizeSets{arg alize, thresh;
		this.waveset.shouldNormalize = true;
		this.waveset.normalizeAmount = alize;
		this.waveset.normalizeThresh = thresh;
	}

	shrinkSets{arg amt;
		this.waveset.shouldShrink = true;
		this.waveset.shrinkAmt = amt;
	}

	customPan{arg norm, pan, dir;
		this.waveset.pan = dir;
		this.waveset.shouldPan = true;
		this.waveset.panLevel = pan;
		this.waveset.panNormLevel = norm;
	}

	customPhrase{arg breakAmountBottom, breakAmountTop, breakBottom, breakTop, swapTime;
		this.waveset.decBreak = true;
		this.contBreak = true;
		this.waveset.breakAmount = {rrand(breakAmountBottom, breakAmountTop)};
		this.breakBottom = breakBottom;
		this.breakTop = breakTop;
		this.breakSwap(swapTime);
	}

	fadeOut{
		this.waveset.decBreak = true;
		this.waveset.breakAmount = {rrand(30, 30)};
		this.contBreak = false;
	}

	breakSwap{arg waitAmount = 2;
		this.waveset.breakPoint = this.breakTop;
		Routine({
			while({this.contBreak.value()},{
				this.waveset.breakPointSet = rrand(this.breakBottom, this.breakTop);
				waitAmount.wait;
			});
			this.waveset.breakPointSet = this.breakBottom;
		}).play;
	}

	playSeries{arg base = 1, waitAmount = 0.5;
		var notes = [
			base,
			base + 0.5825,
			base +  0.5825 - 0.2499,
			base + 0.5825 - 0.2499 - (0.083/2),
			base + 0.5825 - 0.2499,
			base + 0.5825 - 0.2499 + 0.415,
			base - 0.2499 + 0.415
		];
		this.shouldPlaySeries = true;
		Routine({
			while({this.shouldPlaySeries},{
				notes.do({arg note;
					this.waveset.baseSpeed = {note};
					waitAmount.wait;
				});
				if(this.shouldScrambleSeries, {
					notes = notes.scramble;
				});
			});
			this.waveset.baseSpeed = {base};
		}).play;
	}

	playMode{arg base = 1, waitAmount = 0.5;
		var major = [
			base,
			base + 0.083,
			base + 0.083*2,
			base + (0.083*2) + (0.083/2),
			base + (0.083*3),
			base + (0.083*4),
			base + (0.083*5),
		];
		var aeo = [
			base,
			base + 0.083,
			base + 0.083 + (0.083/2),
			base + (0.083*2),
			base + (0.083*3),
			base + (0.083*3) + (0.083/2),
			base + (0.083*5),
			base + (0.083*6),
		];
		var dor = [
			base,
			base + 0.083,
			base + (0.083*2),
			base + (0.083*2) + (0.083/2),
			base + (0.083*4),
			base + (0.083*5),
			base + (0.083*6),
			base + (0.083*6) + (0.083/2),
		];
		var minor = [
			base,
			base + 0.083,
			base + (0.083*2) + (0.083/2),
			base + (0.083*3),
			base + (0.083*4),
			base + (0.083*5),
			base + (0.083*6) + (0.083/2),
			base + (0.083*7),
		];
		var notes = [dor, aeo, minor, major];
		var useNotes = notes.choose;
		this.shouldPlayMode = true;
		Routine({
			while({this.shouldPlayMode},{
				if(this.shouldSwitchMode, {useNotes = notes.choose});
				useNotes = [useNotes.reverse, useNotes.scramble, useNotes].choose;
				useNotes.do({arg note;
					this.waveset.baseSpeed = {note};
					waitAmount.wait;
				});
			});
			this.waveset.baseSpeed = {base};
		}).play;
	}

	speedRandomDirect{arg waitAmount = 0.5, speedStart, minSpeed, maxSpeed, direction = 0;
		this.contSpeedDirectBreak = true;
		Routine({
			var origSpeed = speedStart ?? this.waveset.baseSpeed.value();
			var speedMultiple;
			var i = 1;
			var defBottom = if(direction === 0, {0.7},{1.1});
			var defTop = if(direction === 0, {0.85},{1.25});
			var speed;
			maxSpeed = maxSpeed ?? this.waveset.baseSpeed.value()*2;
			minSpeed = minSpeed ?? this.waveset.baseSpeed.value()*0.25;
			while({this.contSpeedDirectBreak},{
				if(
					(
						(this.waveset.baseSpeed.value() > (maxSpeed)) or:
						(this.waveset.baseSpeed.value() < (minSpeed));
					), {
						this.waveset.baseSpeed = {origSpeed};
					}
				);
			case
				{ ( (i%5) === 0 ) } {speedMultiple = rrand(1.05, 1.5);}
				{ ( (i%3) === 0 ) } {speedMultiple = rrand(0.6, 0.9);}
				{ ( (i%7) === 0 ) } {speedMultiple = rrand(1.15, 1.6);}
				{ ( (i%12) === 0 ) } {speedMultiple = rrand(0.5, 0.75);}
				{ ( (i%2) === 0 ) } {speedMultiple = rrand(0.85, 1.15);}
				{ true } {speedMultiple = rrand(defBottom,defTop)};
				speed = this.waveset.baseSpeed.value()*speedMultiple;
				this.waveset.baseSpeed = {speed};
				i = i + 1;
				waitAmount.wait;
			});
			this.waveset.baseSpeed = {origSpeed};
		}).play;
	}

	speedRandom{arg waitAmount = 0.5, speedStart, minSpeed, maxSpeed;
		this.contSpeedRandomBreak = true;
		Routine({
			var origSpeed = speedStart ?? this.waveset.baseSpeed.value();
			var speedMultiple;
			var i = 1;
			var speed;
			maxSpeed = maxSpeed ?? this.waveset.baseSpeed.value()*2;
			minSpeed = minSpeed ?? this.waveset.baseSpeed.value()*0.25;
			while({this.contSpeedRandomBreak},{
				if(
					(
						(this.waveset.baseSpeed.value() > (maxSpeed)) or:
						(this.waveset.baseSpeed.value() < (minSpeed));
					), {
						this.waveset.baseSpeed = {origSpeed};
					}
				);
			case
				{ ( (i%5) === 0 ) } {speedMultiple = rrand(1.05, 1.4);}
				{ ( (i%3) === 0 ) } {speedMultiple = rrand(0.6, 0.9);}
				{ ( (i%7) === 0 ) } {speedMultiple = rrand(1.15, 1.28);}
				{ ( (i%12) === 0 ) } {speedMultiple = rrand(0.5, 0.85);}
				{ ( (i%2) === 0 ) } {speedMultiple = rrand(1.1, 1.19);}
				{ true } {speedMultiple = rrand(0.9, 0.99)};
				speed = this.waveset.baseSpeed.value()*speedMultiple;
				this.waveset.baseSpeed = {speed};
				i = i + 1;
				waitAmount.wait;
			});
			this.waveset.baseSpeed = {origSpeed};
		}).play;
	}

	multiplierRandomDirect{arg waitAmount = 0.5, multiplierStart, minMultiplier, maxMultiplier, direction = 0;
		this.contMultiplierDirectBreak = true;
		Routine({
			var origMultiplier = multiplierStart ?? this.waveset.multiplier.value();
			var multiplierMultiple;
			var i = 1;
			var defBottom = if(direction === 0, {0.7},{1.1});
			var defTop = if(direction === 0, {0.85},{1.25});
			var multiple;
			maxMultiplier = maxMultiplier ?? this.waveset.multiplier.value()*2;
			minMultiplier = minMultiplier ?? this.waveset.multiplier.value()*0.25;
			while({this.contMultiplierDirectBreak},{
				if(
					(
						(this.waveset.multiplier.value() > (maxMultiplier)) or:
						(this.waveset.multiplier.value() < (minMultiplier));
					), {
						this.waveset.multiplier = {origMultiplier};
					}
				);
				case
				{ ( (i%3) === 0 ) } { multiplierMultiple = rrand(0.7, 0.9);}
				{ ( (i%5) === 0 ) } { multiplierMultiple = rrand(1.05, 1.3);}
				{ ( (i%12) === 0 ) } { multiplierMultiple = rrand(0.4, 0.9);}
				{ ( (i%7) === 0 ) } { multiplierMultiple = rrand(1.15, 1.6);}
				{ ( (i%2) === 0 ) } {multiplierMultiple = rrand(0.85, 1.1);}
				{ true } {multiplierMultiple = rrand(defBottom,defTop)};
				multiple = this.waveset.multiplier.value()*multiplierMultiple;
				this.waveset.multiplier = {multiple};
				i = i + 1;
				waitAmount.wait;
			});
			this.waveset.multiplier = {origMultiplier};
		}).play;
	}

	multiplierRandom{arg waitAmount = 0.5, multiplierStart, minMultiplier, maxMultiplier;
		this.contMultiplierRandomBreak = true;
		Routine({
			var origMultiplier = multiplierStart ?? this.waveset.multiplier.value();
			var multiplierMultiple;
			var i = 1;
			var multiple;
			maxMultiplier = maxMultiplier ?? this.waveset.multiplier.value()*2;
			minMultiplier = minMultiplier ?? this.waveset.multiplier.value()*0.25;
			while({this.contMultiplierRandomBreak},{
				if(
					(
						(this.waveset.multiplier.value() > (maxMultiplier)) or:
						(this.waveset.multiplier.value() < (minMultiplier));
					), {
						this.waveset.multiplier = {origMultiplier};
					}
				);
				case
				{ ( (i%3) === 0 ) } { multiplierMultiple = rrand(0.7, 0.9);}
				{ ( (i%5) === 0 ) } { multiplierMultiple = rrand(1.05, 1.3);}
				{ ( (i%12) === 0 ) } { multiplierMultiple = rrand(0.6, 0.8);}
				{ ( (i%7) === 0 ) } { multiplierMultiple = rrand(1.15, 1.3);}
				{ ( (i%2) === 0 ) } {multiplierMultiple = rrand(1.1, 1.35);}
				{ true } {multiplierMultiple = rrand(0.9,0.99)};
				multiple = this.waveset.multiplier.value()*multiplierMultiple;
				this.waveset.multiplier = {multiple};
				i = i + 1;
				waitAmount.wait;
			});
			this.waveset.multiplier = {origMultiplier};
		}).play;
	}

	startFlop{
		this.sayWow(7,2);
		this.ensound(1,3);
		this.waveset.breakAmount = {rrand(0.001, 0.05)};
		this.waveset.decBreak = true;
		this.contBreak = {true};
		this.breakBottom = 8;
		this.breakTop = 30;
		this.breakSwap(0.02);
	}

	getRhythm{
		var breakAmtBottom, breakAmtTop;
		breakAmtBottom = rrand(0.0009,0.002);
		breakAmtTop = rrand(0.01,0.09);
		this.waveset.decBreak = true;
		this.contBreak = {true};
		this.breakBottom = 3;
		this.breakTop = 20;
		this.breakSwap(rrand(0.01,0.1));
		this.waveset.breakAmount = {rrand(breakAmtBottom, breakAmtTop)};
	}

	slowStrong{
		var breakBottom, breakTop;
		var breakAmtBottom, breakAmtTop;
		breakAmtBottom =  rrand(0.1,1.5);
		breakAmtTop = rrand(1.65, 3.8);
		breakBottom = rrand(200,800);
		breakTop = rrand(900,1800);
		this.waveset.decBreak = true;
		this.contBreak = {true};
		this.breakBottom = breakBottom;
		this.breakTop = breakTop;
		this.breakSwap(rrand(0.01,0.8));
		this.waveset.breakAmount = {rrand(breakAmtBottom, breakAmtTop)};
	}

	changeSubWave{arg waveStr;
		var wave = this.waveset.waves.waves[waveStr];
		this.waveset.subBuf.sendCollection(wave);
	}

	changeCurrSet{arg waveset;
		this.holderCurrSet = this.waveset.currSet;
		this.waveset.switchSet = true;
		this.waveset.switchToSet = waveset;
	}

	changeLeaveOneSet{arg waveset;
		this.holderLeaveOneSet = this.waveset.leaveOneSet;
		this.waveset.leaveOneSet = waveset;
	}

	changeLeaveTwoSet{arg waveset;
		this.holderLeaveTwoSet = this.waveset.leaveTwoSet;
		this.waveset.leaveTwoSet = waveset;
	}

	changeTransferSetOne{arg waveset;
		this.holderTransferSetOne = this.waveset.transferSetOne;
		this.waveset.transferSetOne = waveset;
	}

	changeTransferSetTwo{arg waveset;
		this.holderTransferSetTwo = this.waveset.transferSetTwo;
		this.waveset.transferSetTwo = waveset;
	}

	returnCurrSet{
		this.waveset.switchSet = true;
		this.waveset.switchToSet = this.holderCurrSet;
	}

	returnLeaveOneSet{
		this.waveset.leaveOneSet = this.holderLeaveOneSet;
	}

	returnLeaveTwoSet{
		this.waveset.leaveTwoSet = this.holderLeaveTwoSet;
	}

	returnTransferSetOne{
		this.waveset.transferSetOne = this.holderTransferSetOne;
	}

	returnTransferSetTwo{
		this.waveset.transferSetTwo = this.holderTransferSetTwo;
	}

	takeSnapshot{
		this.snapShot.currFunc = this.currFunc;
		this.snapShot.shouldAverage = this.waveset.shouldAverage;
		this.snapShot.inst = this.waveset.inst;
		this.snapShot.waveSetAmount = this.waveset.waveSetAmount.value();
		this.snapShot.repeats = this.waveset.repeats;
		this.snapShot.baseSpeed = this.waveset.baseSpeed.value();
		this.snapShot.distance = this.waveset.distance;
		this.snapShot.speedChangeModulo = this.waveset.speedChangeModulo;
		this.snapShot.speedMod = this.waveset.speedMod;
		this.snapShot.plusAmount = this.waveset.plusAmount;
		this.snapShot.multiplier = this.waveset.multiplier.value();
		this.snapShot.shouldAverage = this.waveset.shouldAverage;
		this.snapShot.shouldSwap = this.waveset.shouldSwap;
		this.snapShot.shouldShuffle = this.waveset.shouldShuffle;
		this.snapShot.shuffAmount = this.waveset.shuffAmount;
		this.snapShot.shouldDelete = this.waveset.shouldDelete;
		this.snapShot.deleteLevel = this.waveset.deleteLevel;
		this.snapShot.deleteNormLevel = this.waveset.deleteNormLevel;
		this.snapShot.shouldDeletePause = this.waveset.shouldDeletePause;
		this.snapShot.shouldRandomDelete = this.waveset.shouldRandomDelete;
		this.snapShot.deleteReceiver = this.waveset.deleteReceiver;
		this.snapShot.deleteDeviation = this.waveset.deleteDeviation;
		this.snapShot.shouldSub = this.waveset.shouldSub;
		this.snapShot.subLevel = this.waveset.subLevel;
		this.snapShot.subNormLevel = this.waveset.subNormLevel;
		this.snapShot.shouldReverse = this.waveset.shouldReverse;
		this.snapShot.reverseLevel = this.waveset.reverseLevel;
		this.snapShot.reverseNormLevel = this.waveset.reverseNormLevel;
		this.snapShot.pan = this.waveset.pan;
		this.snapShot.shouldPan = this.waveset.shouldPan;
		this.snapShot.panLevel = this.waveset.panLevel;
		this.snapShot.panNormLevel = this.waveset.panNormLevel;
		this.snapShot.decBreak = this.waveset.decBreak;
		this.snapShot.contBreak = this.contBreak.value();
		this.snapShot.breakBottom = this.breakBottom;
		this.snapShot.breakTop = this.breakTop;
		this.snapShot.shouldHarmonize = this.waveset.shouldHarmonize;
		this.snapShot.harmLevel = this.waveset.harmLevel;
		this.snapShot.harmonizeLevel = this.waveset.harmonizeLevel;
		this.snapShot.harmonizeNormLevel = this.waveset.harmonizeNormLevel;
		this.snapShot.shouldTransferOne = this.waveset.shouldTransferOne;
		this.snapShot.transferOneLevel = this.waveset.transferOneLevel;
		this.snapShot.transferOneNormLevel = this.waveset.transferOneNormLevel;
		this.snapShot.shouldTransferTwo = this.waveset.shouldTransferTwo;
		this.snapShot.transferTwoLevel = this.waveset.transferTwoLevel;
		this.snapShot.transferTwoNormLevel = this.waveset.transferTwoNormLevel;
		this.snapShot.shouldInterleaveOne = this.waveset.shouldInterleaveOne;
		this.snapShot.interleaveOneLevel = this.waveset.interleaveOneLevel;
		this.snapShot.interleaveOneNormLevel = this.waveset.interleaveOneNormLevel;
		this.snapShot.shouldInterleaveTwo = this.waveset.shouldInterleaveTwo;
		this.snapShot.interleaveTwoLevel = this.waveset.interleaveTwoLevel;
		this.snapShot.interleaveTwoNormLevel = this.waveset.interleaveTwoNormLevel;
		this.snapShot.shouldNormalize = this.waveset.shouldNormalize;
		this.snapShot.normalizeAmount = this.waveset.normalizeAmount;
		this.snapShot.normalizeThresh = this.waveset.normalizeThresh;
		this.snapShot.speedSwapModulo = this.waveset.speedSwapModulo;
		this.snapShot.speedSwapAmt = this.waveset.speedSwapAmt.value();
		this.snapShot.waveSubMod = this.waveset.waveSubMod;
		this.snapShot.shouldShrink = this.waveset.shouldShrink;
		this.snapShot.shrinkAmt = this.waveset.shrinkAmt;
	}

	showSnapshot{
		this.snapShot.keys.asList.copyRange(0, this.snapShot.keys.size - 1).do({arg prop;
			[prop.asString + " : " +  this.snapShot[prop.asSymbol].asString].postln;
		});
	}
}


