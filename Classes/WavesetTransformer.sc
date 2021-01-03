WavesetTransformer {
	var <>currSet, <>leaveOneSet, <>leaveTwoSet, <>transferSetOne, <>transferSetTwo, <>switchSet, <>switchToSet;

	var <>subBuf, <>mainOut = 0, <>out = 5, <>waves;

	var <>startModFreq, <>startAmt, <>speed2, <>distance, <>speedChangeModulo, <>plusAmount, <>speedMod, <>speedSwapModulo;

	var <>waveSetAmount, <>multiplier, <>repeats, <>baseSpeed, <>shouldSwap, <>pan, <>inst;

	var <>shouldDelete, <>shouldRandomDelete, <>deletePauseSet, <>shouldDeletePause, <>shouldAverage, <>shouldShuffle, <>shouldSub, <>shouldReverse, <>shouldPan, <>shouldHarmonize, <>shouldTransferOne, <>shouldTransferTwo, <>shouldInterleaveTwo, <>shouldInterleaveOne, <>shouldNormalize, <>shouldShrink, <>useCurrBuf;

	var <>deleteLevel, <>deleteAmt, <>deleteNormAmt, <>deleteNormLevel,<>deleteReceiver, <>deleteDeviation, <>averageModulo, <>shuffAmount, <>subAmt, <>subLevel, <>subNormAmt, <>subNormLevel, <>reverseAmt, <>reverseLevel, <>reverseNormAmt, <>reverseNormLevel, <>panAmt, <>panLevel, <>panNormAmt, <>panNormLevel, <>waveSubMod, <>lastAmpAmt, <>lastAmpAdd, <>ampAmt, <>ampAdd, <>harmLevel, <>harmonizeAmt, <>harmonizeLevel, <>harmonizeNormAmt, <>harmonizeNormLevel, <>transferOneAmt, <>transferOneLevel, <>transferOneNormAmt, <>transferOneNormLevel, <>transferTwoAmt, <>transferTwoLevel, <>transferTwoNormAmt, <>transferTwoNormLevel, <>interleaveTwoAmt, <>interleaveOneAmt, <>interleaveOneLevel, <>interleaveOneNormAmt, <>interleaveOneNormLevel, <>interleaveTwoLevel, <>interleaveTwoNormAmt, <>interleaveTwoNormLevel, <>normalizeAmount, <>normalizeThresh, <>speedSwapAmt, <>shrinkAmt;

	var <>breakPointSet, <>breakPoint, <>decBreak, <>breakBottom, <>breakTop, <>breakAmount;

	*new {| subBufNum, files  |
        ^super.new.init(subBufNum, files);
    }

    init { | subBufNum, files, wsOut = 5, mainOut = 0, server |
		var buf = Buffer.alloc(server, 512,bufnum:subBufNum);

		this.out = wsOut;
		this.mainOut = mainOut;

		SynthDef(\eq, {arg in = 5, out = 0;

			var limiter = Limiter.ar(In.ar(in, 2), 0.85);

			Out.ar(out, limiter);
		}).add;

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
		this.transferSetOne = Wavesets.from(files.transferSetOne);
		this.transferSetTwo = Wavesets.from(files.transferSetTwo);
		this.switchSet = false;
		this.switchToSet = Wavesets.from(files.currSet);
		this.setDefaults();

		^this;
    }

	setDefaults{

		this.startModFreq = 0.5;
		this.startAmt = 0.2;
		this.speed2 = 1.1;
		this.distance = 0;
		this.speedChangeModulo = this.currSet.xings.size*2;
		this.plusAmount = 0.0;
		this.speedMod = 0;
		this.speedSwapModulo = this.currSet.xings.size*2;

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
		this.shouldTransferOne = false;
		this.shouldTransferTwo = false;
		this.shouldInterleaveTwo = false;
		this.shouldInterleaveOne = false;
		this.shouldNormalize = false;
		this.shouldShrink = false;
		this.useCurrBuf = false;

		this.deleteLevel = 1;
		this.deleteAmt = 1;
		this.deleteNormAmt = 0;
		this.deleteNormLevel = 1;
		this.deleteReceiver = 1;
		this.deleteDeviation = 2;
		this.averageModulo = this.currSet.xings.size*2;
		this.shuffAmount = 0;
		this.subAmt = 1;
		this.subLevel = 1;
		this.subNormAmt = 0;
		this.subNormLevel = 0;
		this.reverseAmt = 1;
		this.reverseLevel = 1;
		this.reverseNormAmt = 0;
		this.reverseNormLevel = 0;
		this.panAmt = 1;
		this.panLevel = 1;
		this.panNormAmt = 0;
		this.panNormLevel = 0;
		this.waveSubMod = this.currSet.xings.size*2;
		this.lastAmpAmt = 1;
		this.lastAmpAdd = 0;
		this.ampAmt = 1;
		this.ampAdd = 0;
		this.harmLevel = 0.1;
		this.harmonizeAmt = 1;
		this.harmonizeLevel = 1;
		this.harmonizeNormAmt = 0;
		this.harmonizeNormLevel = 0;
		this.transferOneAmt = 1;
		this.transferOneLevel = 1;
		this.transferOneNormAmt = 0;
		this.transferOneNormLevel = 0;
		this.transferTwoAmt = 1;
		this.transferTwoLevel = 1;
		this.transferTwoNormAmt = 0;
		this.transferTwoNormLevel = 0;
		this.interleaveTwoAmt = 1;
		this.interleaveOneAmt = 1;
		this.interleaveOneLevel = 1;
		this.interleaveOneNormAmt = 0;
		this.interleaveOneNormLevel = 0;
		this.interleaveTwoLevel = 1;
		this.interleaveTwoNormAmt = 0;
		this.interleaveTwoNormLevel = 0;
		this.normalizeAmount = this.currSet.xings.size*2;
		this.normalizeThresh = 10;
		this.speedSwapAmt = {1};
		this.shrinkAmt = 1.75;

		this.breakPointSet = 1;
		this.breakPoint = 1;
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
				//the stock market is taking a plunge
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
				//okay time for the regular square
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

				//HNW!
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
					"You need to enter in an amount of waves you will instead get all of the waves".warn;
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
				amount.do({arg i;
					var ev, speed, start, length, startFrame, endFrame, amp,harmEvents,
					numWs, sustain, repeats, sum, origRate, wsDur, frame, type, plunk;

					if( this.shouldInterleaveTwo && (this.interleaveTwoNormAmt > 0)){
						i = i - this.interleaveTwoAmt;
					};

					if(this.shouldShuffle, {
						var origI = i;
						i = i + this.shuffAmount.rand2;
						if( (i < 0) or: (i >= amount)){
							if( i < 0 ){
								i = origI + this.shuffAmount;
							};

							if( i >= amount ){
								i = origI - this.shuffAmount;
							};
						};

						if( (i < 0) or: (i >= amount)){
							i = origI;
						}
					});

					speed = this.baseSpeed.value();


					if( (i%this.speedChangeModulo) === 0, {
					 	speed = speed + ((i%this.speedMod) * this.plusAmount);
					 },{
					 	speed = speed - ((i%this.speedMod) * this.plusAmount);
					});

					if( (i%this.speedSwapModulo) === 0){
						speed = speed*this.speedSwapAmt();
					};

					repeats = this.repeats.value();
					numWs = this.waveSetAmount.value();

					//////WELCOME TO THE MAINNNN EVENTTTTTTT
					ev = this.currSet.eventFor(i, numWs, repeats, speed);

					startFrame = this.currSet.xings.clipAt(i);
					endFrame = this.currSet.xings.clipAt(i + numWs);
					ev.length = (endFrame - startFrame);

					if( ((i%this.averageModulo) === 0) && this.shouldAverage, {
						ev.length = this.currSet.avgLength;
						ev.wsAmp = this.currSet.avgAmp;
					});


					if(this.shouldTransferOne){
						if(this.transferOneAmt > 0){
							var transferStart;

							if(numWs > (this.transferSetOne.xings.size - 1)){
								numWs = (this.transferSetOne.xings.size - 1) - this.waveSetAmount.value();
							};

							transferStart = i%(this.transferSetOne.xings.size - 1);

							ev = this.transferSetOne.eventFor(transferStart, numWs, repeats, speed);
							ev.buf = this.currBuf();

							this.transferOneAmt = this.transferOneAmt - 1;

							if(this.transferOneAmt === 0){
								this.transferOneNormAmt = this.transferOneNormLevel + 1;
							};

						};

						if(this.transferOneNormAmt > 0){
							this.transferOneNormAmt = this.transferOneNormAmt - 1;
							if(this.transferOneNormLevel === 0){
								this.transferOneAmt = this.transferOneLevel;
							};
						};

					};


					if(this.shouldTransferTwo){
						if(this.transferTwoAmt > 0){
							var transferStart, tempLength, tempAmp;
							tempLength = ev.length;
							tempAmp = ev.wsAmp;
							if(numWs > (this.transferSetTwo.xings.size - 1)){
								numWs = (this.transferSetTwo.xings.size - 1) - this.waveSetAmount.value();
							};

							transferStart = i%(this.transferSetTwo.xings.size - 1);

							ev = this.transferSetTwo.eventFor(transferStart, numWs, repeats, speed);
							ev.length = tempLength;
							ev.wsAmp = tempAmp;

							this.transferTwoAmt = this.transferTwoAmt - 1;
							if(this.transferTwoAmt === 0){
								this.transferTwoNormAmt = this.transferTwoNormLevel + 1;
							};

						};

						if(this.transferTwoNormAmt > 0){
							this.transferTwoNormAmt = this.transferTwoNormAmt - 1;
							if(this.transferTwoNormAmt === 0){
								this.transferTwoAmt = this.transferTwoLevel;
							};
						};

					};

					if(this.shouldInterleaveOne){
						if(this.interleaveOneAmt > 0){
							var leaveStart;

							if(numWs > (this.leaveOneSet.xings.size - 1)){
								numWs = (this.leaveOneSet.xings.size - 1) - this.waveSetAmount.value();
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
								numWs = (this.leaveTwoSet.xings.size - 1) - this.waveSetAmount.value();
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


					ev.length = ev.length*this.multiplier.value();

					if( ( (i%2) == 0 && this.shouldSwap), {
						ev.length = (endFrame - startFrame)/this.multiplier.value();
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

						if( ((i%this.waveSubMod) === 0 && (i > 1)), {
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
						\out, this.out,
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
								temp.amp = temp.amp / (i*this.harmLevel);
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
						if(harmEvents.notNil, {
							harmEvents.do({arg harm;
								harm.play;
							});
						});

					if(i !== (this.currSet.xings.size - (this.waveSetAmount.value() + 1)), {
							(ev.sustain + this.distance).wait;
						},{
							this.distance.wait;
						});
					if((this.shouldDeletePause && this.deletePauseSet), {
						this.deletePauseSet = false;
						if(i !== (this.currSet.xings.size - (this.waveSetAmount.value() + 1)), {
							(ev.sustain + this.distance).wait;
						},{
							this.distance.wait;
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
						this.breakAmount.value().wait;
						this.breakPoint = this.breakPointSet;
					    this.ampAmt = this.lastAmpAmt;
						this.ampAdd = this.lastAmpAdd;
					});

					harmEvents = nil;

				});

			});
		});
	}

}










