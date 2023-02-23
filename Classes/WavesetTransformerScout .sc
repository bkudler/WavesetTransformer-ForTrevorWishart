WavesetTransformerScout : Scout {

    *new {
		^super.new.init();
    }

	init {
		actions = (
			\wavesetEnd: [],
			\wavesetGroupStart: [],
			\wavesetGroupEnd: [],
			\rampEnds: [],
			\wavesetLoopEnd: [],
			\wavesetLoopsFinshed: []
		);
	}

	prActionNotUnderstoodError{
		"this is not a waveset scout action".error;
		^nil;
	}


}

