WavesetTransformer {
	var <>currSet, <>leaveOneSet, <>leaveTwoSet, <>transferSet, <>switchSet, <>switchToSet;

	var <>subBuf, <>outToMain, <>waves, <>fxOut, <>trueOutToMain;

	var <>startModFreq, <>startAmt, <>speed2, <>distance, <>speedChangeModulo, <>plusAmount, <>speedMod, <>speedSwapModulo, <>longSwapAmt, <>longSwapNormLevel, <>longSwapNormAmt, <>longSwapLevel, <>longSwapAmt, <>longSwapLevel, <>longSwapNormAmt, <>longSwapNormLevel, <>multiLongSwapAmt, <>multiLongSwapLevel, <>multiLongSwapNormAmt, <>multiLongSwapNormLevel, <>repeatSwapAmt, <>repeatSwapLevel, <>repeatSwapNormAmt,  <>repeatSwapNormLevel;

	var <>waveSetAmount, <>multiplier, <>repeats, <>baseSpeed, <>shouldSwap, <>pan, <>inst;

	var <>shouldDelete, <>shouldRandomDelete, <>deletePauseSet, <>shouldDeletePause, <>shouldAverage, <>shouldShuffle, <>shouldSub, <>shouldReverse, <>shouldPan, <>shouldHarmonize, <>shouldTransfer, <>shouldInterleaveTwo, <>shouldInterleaveOne, <>shouldNormalize, <>shouldShrink, <>shouldLongSwap, <>useCurrBuf, <>shouldMultiLongSwap, <>shouldRepeatSwap, <>shouldFx;

	var <>deleteLevel, <>deleteAmt, <>deleteNormAmt, <>deleteNormLevel,<>deleteReceiver, <>deleteDeviation, <>averageModulo, <>shuffAmount, <>subAmt, <>subLevel, <>subNormAmt, <>subNormLevel, <>reverseAmt, <>reverseLevel, <>reverseNormAmt, <>reverseNormLevel, <>panAmt, <>panLevel, <>panNormAmt, <>panNormLevel, <>waveSubMod, <>lastAmpAmt, <>lastAmpAdd, <>ampAmt, <>ampAdd, <>harmLevel, <>harmonizeAmt, <>harmonizeLevel, <>harmonizeNormAmt, <>harmonizeNormLevel, <>transferAmt, <>transferLevel, <>transferNormAmt, <>transferNormLevel, <>interleaveTwoAmt, <>interleaveOneAmt, <>interleaveOneLevel, <>interleaveOneNormAmt, <>interleaveOneNormLevel, <>interleaveTwoLevel, <>interleaveTwoNormAmt, <>interleaveTwoNormLevel, <>normalizeAmount, <>normalizeThresh, <>speedSwapAmt, <>shrinkAmt, <>multiSwapAmt, <>repeatSwapLev, <>fxAmt, <>fxNormAmt, <>fxNormLevel, <>fxLevel ;

	var <>breakPointSet, <>breakPoint, <>decBreak, <>breakBottom, <>breakTop, <>breakAmount;

	var <ws_scout;

	*new {| subBufNum, files, mainOut, fxOut |
        ^super.new.init(subBufNum, files, mainOut, fxOut);
    }

    init { | subBufNum, files, mainOut, fxOut, server|
		var buf = Buffer.alloc(server, 512,bufnum:subBufNum);

		if(mainOut.isNil, {mainOut = 0});
		if(fxOut.isNil, {fxOut = 0});

		ws_scout = WavesetTransformerScout();
		this.outToMain = mainOut;
		this.trueOutToMain = mainOut;
		this.fxOut = fxOut;


		Wavesets.prepareSynthDefs;

		SynthDef(\wstSTM, { | out = 0, buf = 0, start = 0, length = 441, playRate = 1, sustain = 1, amp = 0.2, pan, startModFreq =0.5, startAmt = 0.2 |
			var startMod = LFPar.kr(startModFreq, 0.45, mul:start*startAmt,add:start);
			var phasor = Phasor.ar(0, BufRateScale.ir(buf) * playRate, 0, length) + startMod;
			var env = EnvGen.ar(Env([amp, amp, 0], [sustain, 0]), doneAction: 2);
			var snd = BufRd.ar(1, buf, phasor) * env;
			OffsetOut.ar(out, Pan2.ar(snd, pan));
		}, \ir.dup(8)).add;

		SynthDef(\wstSTM1gl, { | out = 0, buf = 0, start = 0, length = 441, playRate = 1, sustain = 1, amp = 0.2, pan, startModFreq = 0.5, startAmt = 0.2, playRate2 = 1 |
			var playRateEnv = Line.ar(playRate, playRate2, sustain);
			var startMod = LFPar.kr(startModFreq, 0.45, mul:start*startAmt,add:start);
			var phasor = Phasor.ar(0, BufRateScale.ir(buf) * playRateEnv, 0, length) + startMod;
			var env = EnvGen.ar(Env([amp, amp, 0], [sustain, 0]), doneAction: 2);
			var snd = BufRd.ar(1, buf, phasor) * env;
			OffsetOut.ar(out, Pan2.ar(snd, pan));
		}, \ir.dup(8)).add;

		this.subBuf = buf;
		this.waves = this.getWaves();

		this.currSet = Wavesets.from(files.currSet);
		this.leaveOneSet = Wavesets.from(files.leaveOneSet);
		this.leaveTwoSet = Wavesets.from(files.leaveTwoSet);
		this.transferSet = Wavesets.from(files.transferSet);
		this.switchSet = false;
		this.switchToSet = Wavesets.from(files.currSet);
		this.setDefaults();

		^this;
    }

	setDefaults{

		this.startModFreq = 0.5;
		this.startAmt = 0.2;
		this.speed2 = 1.1;
		this.distance = {0};
		this.speedChangeModulo = this.currSet.xings.size*2;
		this.plusAmount = {0.0};
		this.speedMod = {0};
		this.speedSwapModulo = this.currSet.xings.size*2;
		this.longSwapAmt = 1;
		this.longSwapLevel = 1;
		this.longSwapNormAmt = 0;
		this.longSwapNormLevel = 0;
		this.multiLongSwapAmt = 1;
		this.multiLongSwapLevel = 1;
		this.multiLongSwapNormAmt = 0;
		this.multiLongSwapNormLevel = 0;
		this.repeatSwapAmt = 1;
		this.repeatSwapLevel = 1;
		this.repeatSwapNormAmt = 0;
		this.repeatSwapNormLevel = 0;

		this.waveSetAmount = {1};
		this.multiplier = {1};
		this.repeats = {1};
		this.baseSpeed = {1};
		this.shouldSwap = false;
		this.inst = Wavesets.defaultInst;

		this.shouldDelete = false;
		this.shouldRandomDelete = false;
		this.deletePauseSet = false;
		this.shouldDeletePause = false;
		this.shouldAverage = false;
		this.shouldShuffle = false;
		this.shouldSub = false;
		this.shouldReverse = false;
		this.shouldPan = false;
		this.shouldHarmonize = false;
		this.shouldTransfer = false;
		this.shouldInterleaveTwo = false;
		this.shouldInterleaveOne = false;
		this.shouldNormalize = false;
		this.shouldShrink = false;
		this.useCurrBuf = false;
		this.shouldLongSwap = false;
		this.shouldMultiLongSwap = false;
		this.shouldRepeatSwap = false;
		this.shouldFx = false;


		this.deleteLevel = 1;
		this.deleteAmt = 1;
		this.deleteNormAmt = 0;
		this.deleteNormLevel = 1;
		this.deleteReceiver = 1;
		this.deleteDeviation = 2;
		this.averageModulo = this.currSet.xings.size*2;
		this.shuffAmount = {0};
		this.subAmt = 1;
		this.subLevel = 1;
		this.subNormAmt = 0;
		this.subNormLevel = 0;
		this.reverseAmt = 1;
		this.reverseLevel = 1;
		this.reverseNormAmt = 0;
		this.reverseNormLevel = 0;
		this.panAmt = 1;
		this.pan = 0;
		this.panLevel = 1;
		this.panNormAmt = 0;
		this.panNormLevel = 0;
		this.waveSubMod = {this.currSet.xings.size*2};
		this.lastAmpAmt = 1;
		this.lastAmpAdd = 0;
		this.ampAmt = 1;
		this.ampAdd = 0;
		this.harmLevel = {0.1};
		this.harmonizeAmt = 1;
		this.harmonizeLevel = 1;
		this.harmonizeNormAmt = 0;
		this.harmonizeNormLevel = 0;
		this.transferAmt = 1;
		this.transferLevel = 1;
		this.transferNormAmt = 0;
		this.transferNormLevel = 0;
		this.interleaveTwoAmt = 1;
		this.interleaveOneAmt = 1;
		this.interleaveOneLevel = 1;
		this.interleaveOneNormAmt = 0;
		this.interleaveOneNormLevel = 0;
		this.interleaveTwoLevel = 1;
		this.interleaveTwoNormAmt = 0;
		this.interleaveTwoNormLevel = 0;
		this.fxAmt = 1;
		this.fxLevel = 1;
		this.fxNormAmt = 0;
		this.fxNormLevel = 0;
		this.normalizeAmount = this.currSet.xings.size*2;
		this.normalizeThresh = 10;
		this.speedSwapAmt = {1};
		this.multiSwapAmt = {1};
		this.repeatSwapLev = {0};
		this.shrinkAmt = 1.75;

		this.breakPointSet = 1;
		this.breakPoint = 50;
		this.decBreak = false;
		this.breakAmount = {0};
	}

	getWaves{
		^(
			waves: (
				saw: Signal.sineFill(512, Array.fill(512, {arg i; i = i + 1; (1/i) - 1} )),
				wuTang:  Signal.sineFill(512, Array.fill(20, {arg i; i = i + 1; (2/i)} ), [1,2]),
				squigSaw:  Signal.sineFill(512, Array.fill(20, {arg i; i = i + 1; (1/i ) } ), Array.fill(20, {arg i; i - 2})),
				weirdSin: Signal.sineFill(512, [1,2]),
				weirdSin2: Signal.sineFill(512, Array.fill(5, {arg i; (i - 1) * -1}), Array.fill(2, {arg i; i })),
				badStocks: Signal.sineFill(512, Array.fill(512, {arg i; i = i + 1; (i*0.5) + rrand(i, i + 2)/i})),
				sin: Signal.sineFill(512, [1]),
				highSin: Signal.sineFill(512, [20]),
				weirdSqr: Signal.sineFill(512, Array.fill(512, {
					arg i;
					if( (i%2 !== 0), {
						1/i;
					},{
						0;
					});
				})),
				sqr: Signal.sineFill(512, Array.fill(512, {
					arg i;
					i = i + 1;
					if( (i%2 !== 0), {
						1/i;
					},{
						0;
					});
				})),
				sizz: Signal.sineFill(512, Array.fill(512,{arg i;
					var div;
					i = i + 1;
					if( (i < (512/2)), {
						(i/2)/i;
					}, {
						i;
					});
				}), Array.fill(20, {arg i; i = i+1; rrand(-1,i)}))*0.8,
				iCallItM: Signal.sineFill(512, Array.fill(512,{arg i;
					var div;
					i = i + 1;
					div = (i*rrand(0.01, 0.2));
					if( (i < (512/2)), {
						i/div;
					}, {
						i;
					});
				}),Array.fill(50, {arg i; i = i+1; rrand(1,i)})),
				skwagSaw: Signal.sineFill(512, Array.fill(512, {arg i;
					i = i + 1;
					if( (i%32 !== 0), {
						if( (i > (512*(0.75))), {
							(1/(i*0.85));
						},{
							if( (i%2) == 0, {
								(1/i)*(-1);
							},{
								(1/i);
							});
						});
					},{
						if( (i > (512*(0.75))), {
							(0.25)
						},{
							0;
						});
					});
				})),
				switchSqr: Signal.sineFill(512, Array.fill(512, {arg i;
					i = i + 1;
					if( (i%4 !== 0), {
						512 - i;
					},{
						0;
					});
				})),
				hNW: Signal.sineFill(512, Array.fill(512, {1.0.rand2})),
				sizzDown: Signal.sineFill(512, Array.fill(512, {arg i;
					i = i + 1;
					if( (i%32 !== 0), {
						if( (i > (512*(0.75))), {
							(1/(i*0.85));
						},{
							(1/i);
						});
					},{
						if( (i > (512*(0.75))), {
							(0.25)
						},{
							0;
						});
					});
				})),
				crazyu: Signal.sineFill(512, Array.fill(512, {
					arg i;
					i = i + 1;
					if( (i%2 !== 0), {
						1/i;
					},{
						0;
					});
				}),[pi, 0, -pi]),
				tri: Signal.sineFill(512, Array.fill(512, {
					arg i;
					i = i + 1;
					if( (i%2 !== 0), {
						(1/i.squared)*(-1);
					},{
						0;
					});
				}),[pi/2]),
				weirdTri: Signal.fill(512, {arg i;
					(512%i)*0.01;
				}),
				clawNOISE: FloatArray.fill(512, {arg i;
					var mod = poisson(i);
					(512%mod)*(-1);
				}).normalize(-1,1),
			),


			showKeys: {arg self, amount;
				if(amount.isNil){
					"You need to enter in an amount of waves, now you will instead get all of the waves".warn;
					amount = self.waves.keys.size;
				};
				if(amount > self.waves.keys.size){
					"You have entered in amount greater than the number of waves. The amount is truncated to the number of waves".warn;
					amount = self.waves.keys.size;
				};
				self.waves.keys.asList.copyRange(0, amount - 1).do({ arg wave;
					wave.postln;
				});
			},

			waveAmount: {arg self;
				self.waves.keys.size.postln;
			},

			showRandomKeys: {arg self;
				var start = rrand(0, self.keys.size - 2);
				var finish = rrand(start, self.keys.size - 1);
				self.waves.keys.asList.copyRange(start, finish).do({ arg wave;
					wave.postln;
				});
			};
		)
	}

	currBuf{
		^this.currSet.buffer.bufnum;
	}

	modulateStart{
		this.inst = \wstSTM;
	}

	modulateSpeed{
		this.inst = \wvst1gl;
	}

	moduleSpeedPlusStart{
		this.inst = \wstSTM1gl;
	}

	newWaves{| wave |
		this.subBuf.sendCollection(wave);
		^wave;
	}

	randomWave{
		this.subBuf.sendCollection(this.waves.waves.choose);
	}

	randomDeleteAmount{
		var numb = gauss(this.deleteReceiver, this.deleteDeviation).asInteger;
		if(numb.isNegative,{
			numb = numb * -1;
			^numb;
		}, {
			^numb;
		});
		if(numb === 0,{
			^this.randomDeleteAmount();
		},{
			^numb;
		});
	}

	setAmpAmt{ |amp|
		this.lastAmpAmt = amp;
		this.ampAmt = amp;
	}

	setAmpAdd{ |amp|
		this.lastAmpAdd = amp;
		this.ampAdd = amp;
	}

	resetInst{
		this.inst = Wavesets.defaultInst;
	}


	go {|length = 2|
		^Routine({
			length.do({arg j;

				var amount;

				if(this.switchSet, {
					this.currSet = this.switchToSet;
				});
				amount = this.currSet.xings.size -  (this.waveSetAmount.value());
				ws_scout.call_action(\wavesetLoopsStart);
				amount.do({arg i;
					var ev, speed, start, length, startFrame, endFrame, amp,harmEvents,
					numWs, sustain, repeats, sum, origRate, wsDur, multiplier;

					if( this.shouldInterleaveTwo && (this.interleaveTwoNormAmt > 0)){
						i = i - this.interleaveTwoAmt;
					};

					if(this.shouldShuffle, {
						var origI = i;
						i = i + this.shuffAmount.value().rand2;
						if( (i < 0) or: (i >= amount)){
							if( i < 0 ){
								i = origI + this.shuffAmount.value();
							};

							if( i >= amount ){
								i = origI - this.shuffAmount.value();
							};
						};

						if( (i < 0) or: (i >= amount)){
							i = origI;
						}
					});

					speed = this.baseSpeed.value();



					if( (i%this.speedChangeModulo) === 0, {
						speed = speed + ((i%this.speedMod.value()) * this.plusAmount.value());
					 },{
						speed = speed - ((i%this.speedMod.value()) * this.plusAmount.value());
					});

					if( (i%this.speedSwapModulo) === 0){
						speed = speed*this.speedSwapAmt();
					};


					if(this.shouldLongSwap){
						if(this.longSwapAmt > 0){

							speed = speed*this.speedSwapAmt();

							this.longSwapAmt = this.longSwapAmt - 1;
							if(this.longSwapAmt === 0){
								this.longSwapNormAmt = this.longSwapNormLevel + 1;
							};

						};

						if(this.longSwapNormAmt > 0){
							this.longSwapNormAmt = this.longSwapNormAmt - 1;
							if(this.longSwapNormAmt === 0){
								this.longSwapAmt = this.longSwapLevel;
							};
						};

					};


					repeats = this.repeats.value();

					if(this.shouldRepeatSwap){
						if(this.repeatSwapAmt > 0){

							repeats = repeats + this.repeatSwapLev;

							this.repeatSwapAmt = this.repeatSwapAmt - 1;
							if(this.repeatSwapAmt === 0){
								this.repeatSwapNormAmt = this.repeatSwapNormLevel + 1;
							};

						};

						if(this.repeatSwapNormAmt > 0){
							this.repeatSwapNormAmt = this.repeatSwapNormAmt - 1;
							if(this.repeatSwapNormAmt === 0){
								this.repeatSwapAmt = this.repeatSwapLevel;
							};
						};

					};

					numWs = this.waveSetAmount.value();

					//////WELCOME TO THE MAIN EVENT
					ev = this.currSet.eventFor(i, numWs, repeats, speed);

					startFrame = this.currSet.xings.clipAt(i);
					endFrame = this.currSet.xings.clipAt(i + numWs);
					ev.length = (endFrame - startFrame);

					if( ((i%this.averageModulo) === 0) && this.shouldAverage, {
						ev.length = this.currSet.avgLength;
						ev.wsAmp = this.currSet.avgAmp;
					});




					if(this.shouldTransfer){
						if(this.transferAmt > 0){
							var transferStart, tempLength, tempAmp;
							tempLength = ev.length;
							tempAmp = ev.wsAmp;

							if(numWs > (this.transferSet.xings.size - 1)){
								numWs = (this.transferSetTwo.xings.size - 1);
							};

							transferStart = i%(this.transferSet.xings.size - 1);

							ev = this.transferSet.eventFor(transferStart, numWs, repeats, speed);
							ev.length = tempLength;
							ev.wsAmp = tempAmp;

							this.transferAmt = this.transferAmt - 1;
							if(this.transferAmt === 0){
								this.transferNormAmt = this.transferNormLevel + 1;
							};

						};

						if(this.transferNormAmt > 0){
							this.transferNormAmt = this.transferNormAmt - 1;
							if(this.transferNormAmt === 0){
								this.transferAmt = this.transferLevel;
							};
						};

					};

					if(this.shouldInterleaveOne){
						if(this.interleaveOneAmt > 0){
							var leaveStart;

							if(numWs > (this.leaveOneSet.xings.size - 1)){
								numWs = (this.leaveOneSet.xings.size - 1);
							};

							leaveStart = i%(this.leaveOneSet.xings.size - 1);

							ev = this.leaveOneSet.eventFor(leaveStart, numWs, repeats, speed);

							this.interleaveOneAmt = this.interleaveOneAmt - 1;

							if(this.interleaveOneAmt === 0){
								this.interleaveOneNormAmt = this.interleaveOneNormLevel + 1;
							};

						};

						if(this.interleaveOneNormAmt > 0){
							this.interleaveOneNormAmt = this.interleaveOneNormAmt - 1;
							if(this.interleaveOneNormAmt === 0){
								this.interleaveOneAmt = this.interleaveOneLevel;
							};
						};

					};


					if(this.shouldInterleaveTwo){
						if(this.interleaveTwoAmt > 0){
							var leaveStart;

							if(numWs > (this.leaveTwoSet.xings.size - 1)){
								numWs = (this.leaveTwoSet.xings.size - 1);
							};

							leaveStart = i%(this.leaveTwoSet.xings.size - 1);

							ev = this.leaveTwoSet.eventFor(leaveStart, numWs, repeats, speed);

							this.interleaveTwoAmt = this.interleaveTwoAmt - 1;

							if(this.interleaveTwoAmt === 0){
								this.interleaveTwoNormAmt = this.interleaveTwoNormLevel + 1;
							};

						};

						if(this.interleaveTwoNormAmt > 0){
							this.interleaveTwoNormAmt = this.interleaveTwoNormAmt - 1;
							if(this.interleaveTwoNormAmt === 0){
								this.interleaveTwoAmt = this.interleaveTwoLevel;
							};
						};

					};


					this.outToMain = this.trueOutToMain;

					if(this.shouldFx){
						if(this.fxAmt > 0){

							this.outToMain = this.fxOut;

							this.fxAmt = this.fxAmt - 1;

							if(this.fxAmt === 0){
								this.fxNormAmt = this.fxNormLevel + 1;
							};

						};

						if(this.fxNormAmt > 0){
							this.fxNormAmt = this.fxNormAmt - 1;
							if(this.fxNormAmt === 0){
								this.fxAmt = this.fxLevel;
							};
						};

					};


					multiplier = this.multiplier.value();

					if(this.shouldMultiLongSwap){
						if(this.multiLongSwapAmt > 0){

							multiplier = multiplier * multiSwapAmt;

							this.multiLongSwapAmt = this.multiLongSwapAmt - 1;

							if(this.multiLongSwapAmt === 0){
								this.multiLongSwapNormAmt = this.multiLongSwapNormLevel + 1;
							};

						};

						if(this.multiLongSwapNormAmt > 0){
							this.multiLongSwapNormAmt = this.multiLongSwapNormAmt - 1;
							if(this.multiLongSwapNormAmt === 0){
								this.multiLongSwapAmt = this.multiLongSwapLevel;
							};
						};

					};



					ev.length = ev.length*multiplier;

					if( ( (i%2) == 0 && this.shouldSwap), {
						ev.length = (endFrame - startFrame)/multiplier;
					});

					sustain = ev.length / this.currSet.sampleRate;
					ev.sustain = sustain / speed * repeats;

					if(this.shouldSub){
						if(this.subAmt > 0){
							if(i >= amount, {i = amount - this.waveSetAmount()});
							origRate = ev[\playRate];

							start = ev.start;
							length = ev.length;
							wsDur = ev.sustain;

							ev[\playRate] = origRate*(512/length);
							ev.putPairs([\buf, this.subBuf]);
							ev[\start] = 0;
							ev[\length] = 512;
							ev[\sustain] = wsDur * repeats / origRate.abs;

							this.subAmt = this.subAmt - 1;

							if(this.subAmt === 0){
								this.subNormAmt = this.subNormLevel + 1;
							};

						};

						if(this.subNormAmt > 0){
							this.subNormAmt = this.subNormAmt - 1;
							if(this.subNormAmt === 0){
								this.subAmt = this.subLevel;
							};
						};

						if( ((i%this.waveSubMod.value()) === 0 && (i > 1)), {
							this.randomWave();
						});

					};

					if( ((i%this.normalizeAmount) == 0) && this.shouldNormalize){
						if(ev.wsAmp > (this.currSet.avgAmp*this.normalizeThresh)){
							ev.amp = this.currSet.avgAmp;
							ev.amp = ev.amp + this.ampAdd;
						}
					};


					amp = if(ev.amp.notNil, {ev.amp},{(this.ampAmt*ev.wsAmp) + this.ampAdd});
					ev.putPairs([
						\amp, amp,
						\out, this.outToMain,
						\pan, this.pan,
						\instrument, this.inst,
						\startModFreq, this.startModFreq,
						\startAmt : this.startAmt,
						\playRate2 : this.speed2,
					]);

					if(this.useCurrBuf){ev.putPairs([\buf, this.currBuf()])};

					if(this.shouldReverse){
						if(this.reverseAmt  > 0){
							ev.playRate = ev.playRate * (-1);

							this.reverseAmt = this.reverseAmt - 1;

							if(this.reverseAmt === 0){
								this.reverseNormAmt = this.reverseNormLevel + 1;
							};

						};

						if(this.reverseNormAmt > 0){
							this.reverseNormAmt = this.reverseNormAmt - 1;
							if(this.reverseNormAmt === 0){
								this.reverseAmt = this.reverseLevel;
							};
						};

					};

					if(this.shouldPan){
						if(this.panAmt > 0){
							ev.pan = ev.pan*(-1);

							this.panAmt = this.panAmt - 1;

							if(this.panAmt === 0){
								this.panNormAmt = this.panNormLevel + 1;
							};

						};

						if(this.panNormAmt > 0){
							this.panNormAmt = this.panNormAmt - 1;
							ev.pan = ev.pan*(1);
							if(this.panNormAmt === 0){
								this.panAmt = this.panLevel;
							};
						};

					};

					if(this.shouldHarmonize){
						if(this.harmonizeAmt > 0){

							var temp = ev.copy;
							harmEvents = List(4);
							(2 to: 4).do({arg i;
								temp = temp.copy;
								temp.playRate = (temp.playRate * i);
								temp.amp = temp.amp / (i*this.harmLevel.value());
								harmEvents.add(temp);
							});

							this.harmonizeAmt = this.harmonizeAmt - 1;

							if(this.harmonizeAmt === 0){
								this.harmonizeNormAmt = this.harmonizeNormLevel + 1;
							};

						};

						if(this.harmonizeNormAmt > 0){
							this.harmonizeNormAmt = this.harmonizeNormAmt - 1;
							if(this.harmonizeNormAmt === 0){
								this.harmonizeAmt = this.harmonizeLevel;
							};
						};

					};

					if(this.shouldDelete){
						if(this.deleteAmt > 0){

							ev.putPairs([\amp, 0]);
							this.deletePauseSet = true;

							this.deleteAmt = this.deleteAmt - 1;

							if(this.deleteAmt === 0){
								this.deleteNormAmt = this.deleteNormLevel + 1;
							};

						};
						if(this.deleteNormAmt > 0){
							this.deleteNormAmt = this.deleteNormAmt - 1;
							if(this.deleteNormAmt === 0){
								this.deleteAmt = this.deleteLevel;
							};
						};

					};

					if( this.shouldRandomDelete && ((i%this.randomDeleteAmount()) === 0) ){
						this.deletePauseSet = true;
						ev.putPairs([\amp, 0]);
					};

					if(  (ev.wsAmp < (this.currSet.avgAmp*this.shrinkAmt)) && this.shouldShrink ){
						ev.putPairs([\amp, 0]);
					};
						ev.play;
					    if( (this.breakPoint === this.breakPointSet), {
						   ws_scout.call_action(\wavesetGroupStart);
					    });

						if(harmEvents.notNil, {
							harmEvents.do({arg harm;
								harm.play;
							});
						});

					if(i !== (this.currSet.xings.size - (this.waveSetAmount.value() + 1)), {
						  (ev.sustain + this.distance.value()).wait;
						},{
						  this.distance.value().wait;
						});
					if((this.shouldDeletePause && this.deletePauseSet), {
						this.deletePauseSet = false;
						if(i !== (this.currSet.xings.size - (this.waveSetAmount.value() + 1)), {
							(ev.sustain + this.distance.value()).wait;
						},{
							this.distance.value().wait;
						});
					});
					if( ( this.decBreak and: (this.breakPoint != 0) and: (this.ampAmt > 0.1) ) ,{
						this.breakPoint = this.breakPoint - 1;
						this.ampAmt = this.ampAmt - (this.lastAmpAmt/this.breakPointSet);
						this.ampAdd = this.ampAdd - (this.lastAmpAdd/this.breakPointSet);

						if(this.ampAmt < 0.001, {this.ampAmt = 0});
						if(this.ampAdd < 0.001, {this.ampAdd = 0});

						if(this.breakPoint < 0, {this.breakPoint = this.breakPointSet});

					});


					 if( (this.breakPoint === 0) or: (this.ampAmt < 0.1) , {
						ws_scout.call_action(\wavesetGroupEnd);
						this.breakAmount.value().wait;
					 	this.breakPoint = this.breakPointSet;
					 	this.ampAmt = this.lastAmpAmt;
					 	this.ampAdd = this.lastAmpAdd;
					 });

					harmEvents = nil;
					ws_scout.call_action(\wavesetEnd, [ev.sustain]);

				});

				ws_scout.call_action(\wavesetLoopEnd);

			});

			   ws_scout.call_action(\wavesetLoopsFinshed);
		});
	}

}
