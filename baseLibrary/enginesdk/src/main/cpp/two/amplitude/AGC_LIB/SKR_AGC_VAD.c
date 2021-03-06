#include "SKR_AGC_defines.h"
#include "../../common/functions.h"
#include "AGC_control.h"
#include "SKR_AGC_typedefs.h"


/*
 * Filter Coefficients (C Source) generated by the Filter Design and Analysis Tool
 *
 * Generated by MATLAB(R) 7.10 and the Signal Processing Toolbox 6.13.
 *
 * Generated on: 26-Apr-2017 14:38:18
 *
 */

/*
 * Discrete-Time IIR Filter (real)
 * -------------------------------
 * Filter Structure    : Direct-Form II, Second-Order Sections
 * Number of Sections  : 3
 * Stable              : Yes
 * Linear Phase        : No
 */

/* General type conversion for MATLAB generated C-code  */

/* 
 * Expected path to tmwtypes.h 
 * C:\Program Files\MATLAB\R2010a\extern\include\tmwtypes.h 
 */


const float NUM16[7][3] = {
  {
       0.92999458844,                 0,                 0 
  },
  {
                   1,                -2,                 1 
  },
  {
     0.8464592541088,                 0,                 0 
  },
  {
                   1,                -2,                 1 
  },
  {
     0.8047264613633,                 0,                 0 
  },
  {
                   1,                -2,                 1 
  },
  {
                   1,                 0,                 0 
  }
};

const float DEN16[7][3] = {
  {
                   1,                 0,                 0 
  },
  {
                   1,   -1.833933390648,   0.8860449631123 
  },
  {
                   1,                 0,                 0 
  },
  {
                   1,   -1.669203142931,   0.7166338735042 
  },
  {
                   1,                 0,                 0 
  },
  {
                   1,   -1.586906790832,   0.6319990546216 
  },
  {
                   1,                 0,                 0 
  }
};

const float NUM48[7][3] = {
  {
     0.9785867904298,                 0,                 0 
  },
  {
                   1,                -2,                 1 
  },
  {
     0.9459768560028,                 0,                 0 
  },
  {
                   1,                -2,                 1 
  },
  {
     0.9281204403576,                 0,                 0 
  },
  {
                   1,                -2,                 1 
  },
  {
                   1,                 0,                 0 
  }
};

const float DEN48[7][3] = {
  {
                   1,                 0,                 0 
  },
  {
                   1,   -1.954152267521,    0.960194894198 
  },
  {
                   1,                 0,                 0 
  },
  {
                   1,   -1.889033079395,   0.8948743446166 
  },
  {
                   1,                 0,                 0 
  },
  {
                   1,   -1.853375378449,   0.8591063829819 
  },
  {
                   1,                 0,                 0 
  }
};


const float NUM441[7][3] = {
  {
     0.9765930817841,                 0,                 0 
  },
  {
                   1,                -2,                 1 
  },
  {
     0.9413417959924,                 0,                 0 
  },
  {
                   1,                -2,                 1 
  },
  {
     0.9221245878463,                 0,                 0 
  },
  {
                   1,                -2,                 1 
  },
  {
                   1,                 0,                 0 
  }
};

const float DEN441[7][3] = {
  {
                   1,                 0,                 0 
  },
  {
                   1,   -1.949613452576,   0.9567588745601 
  },
  {
                   1,                 0,                 0 
  },
  {
                   1,   -1.879239842234,   0.8861273417352 
  },
  {
                   1,                 0,                 0 
  },
  {
                   1,   -1.840875729052,   0.8476226223331 
  },
  {
                   1,                 0,                 0 
  }
};

const float NUM32[7][3] = {
  {
     0.9671137416388,                 0,                 0 
  },
  {
                   1,                -2,                 1 
  },
  {
     0.9200661584292,                 0,                 0 
  },
  {
                   1,                -2,                 1 
  },
  {
     0.8949306024085,                 0,                 0 
  },
  {
                   1,                -2,                 1 
  },
  {
                   1,                 0,                 0 
  }
};

const float DEN32[7][3] = {
  {
                   1,                 0,                 0 
  },
  {
                   1,   -1.927500578834,   0.9409543877211 
  },
  {
                   1,                 0,                 0 
  },
  {
                   1,   -1.833732658925,    0.846531974792 
  },
  {
                   1,                 0,                 0 
  },
  {
                   1,   -1.783636381007,   0.7960860286271 
  },
  {
                   1,                 0,                 0 
  }
};

const float NUM24[7][3] = {
  {
     0.9551709978838,                 0,                 0 
  },
  {
                   1,                -2,                 1 
  },
  {
     0.8948586061226,                 0,                 0 
  },
  {
                   1,                -2,                 1 
  },
  {
     0.8633834064306,                 0,                 0 
  },
  {
                   1,                -2,                 1 
  },
  {
                   1,                 0,                 0 
  }
};

const float DEN24[7][3] = {
  {
                   1,                 0,                 0 
  },
  {
                   1,   -1.898509416425,   0.9221745751104 
  },
  {
                   1,                 0,                 0 
  },
  {
                   1,   -1.778631777825,   0.8008026466657 
  },
  {
                   1,                 0,                 0 
  },
  {
                   1,   -1.716071290612,   0.7374623351105 
  },
  {
                   1,                 0,                 0 
  }
};

const float NUM8[7][3] = {
  {
      0.846086879678,                 0,                 0 
  },
  {
                   1,                -2,                 1 
  },
  {
     0.7157374098681,                 0,                 0 
  },
  {
                   1,                -2,                 1 
  },
  {
     0.6572744954233,                 0,                 0 
  },
  {
                   1,                -2,                 1 
  },
  {
                   1,                 0,                 0 
  }
};

const float DEN8[7][3] = {
  {
                   1,                 0,                 0 
  },
  {
                   1,   -1.594640568777,   0.7897069499348 
  },
  {
                   1,                 0,                 0 
  },
  {
                   1,   -1.348967745253,   0.5139818942197 
  },
  {
                   1,                 0,                 0 
  },
  {
                   1,   -1.238781265139,   0.3903167165548 
  },
  {
                   1,                 0,                 0 
  }
};
//600hz bthp@6  
int AGCVAD(int *memSpeech,int *memSilenceCountDown,float memnoiseVAD_db,float avgstax_db,float avgendx_db,float avgx_db)
{
	float Threshold_db;
	int Speech;

	if (memSpeech[0] == 1)
	{
		Threshold_db = memnoiseVAD_db + 0.2*(TVAD_DB - 0.34);
	} 
	else
	{
		Threshold_db = memnoiseVAD_db + TVAD_DB - 0.35;//Threshold_db = mAGC->memnoise_db + T1_DB - 0.3;
	}

	if (memnoiseVAD_db<-42.0)
	{
		//Threshold_db += 2.0;

		if(avgstax_db >= avgendx_db)
		{
			Threshold_db += 0.9;
		} 
		else
		{
			Threshold_db += 0.7;

		}
	}

	if(avgstax_db<avgendx_db)
	{
		Threshold_db -= 0.37;

	}


	Threshold_db = THEMAXOF(Threshold_db,-50.0);//we add -50db noise
	if (Threshold_db>-15)
	{
		Threshold_db = -15;
	}


	if(avgx_db <= Threshold_db && avgx_db < -22.0)//this means if avx_db>=-10db it will be considered to be voice
	{
		memSpeech[0] = 0;
	} 
	else
	{
		memSpeech[0] = 1;

	}
	if (memSpeech[0] == 1)
	{
		Speech = 1;
		memSilenceCountDown[0] = SPEECH_END_PROTECTION;
	} 
	else
	{
		if (memSilenceCountDown[0]>0)
		{
			memSilenceCountDown[0]--;
		}
		if (memSilenceCountDown[0]>0)
		{
			//Threshold_db = (((2.2 - 1)/SPEECH_END_PROTECTION)*memSilenceCountDown[0] + 1) * Threshold_db;
			Threshold_db = ((-54-Threshold_db)/SPEECH_END_PROTECTION)*memSilenceCountDown[0] + Threshold_db;
	
			if(avgstax_db<avgendx_db)
			{
				Threshold_db -= 0.17;

			}
			if(avgstax_db+0.35<avgendx_db)
			{
				Threshold_db -= 0.67;

			}
			//T1 = mAGC->memnoiseVAD_db + 0.15*(TVAD_DB - 0.34);

			if(avgx_db <= Threshold_db||avgx_db <= memnoiseVAD_db - 1.0)
			{
				Speech = 0;
			} 
			else
			{
				Speech = 1;
			}

		} 
		else
		{
			Speech = 0;
		}
	}
	return Speech;
}

int vipVAD(int *memSpeech,int *memSilenceCountDown,float memnoiseVAD_db,float avgstax_db,float avgendx_db,float avgx_db,float vippesvdb,float lasthighdb,float period/*now use it as bool*/)
{
	float Threshold_db;
	int Speech;

	if (memSpeech[0] == 1)
	{
		Threshold_db = memnoiseVAD_db + 14;
	} 
	else
	{
		Threshold_db = memnoiseVAD_db + 18;//Threshold_db = mAGC->memnoise_db + T1_DB - 0.3;
	}

	if (memnoiseVAD_db<-42.0)
	{
		//Threshold_db += 2.0;

		if(avgstax_db >= avgendx_db)
		{
			Threshold_db += 0.9;
		} 
		else
		{
			Threshold_db += 0.7;

		}
	}

	if(avgstax_db<avgendx_db)
	{
		Threshold_db -= 0.37;

	}

	Threshold_db = THEMAXOF(Threshold_db,-50.0);//we add -50db noise
	if (Threshold_db>-15)
	{
		Threshold_db = -15;
	}

	if(avgx_db <= Threshold_db || avgx_db < -17 || avgx_db < vippesvdb - 14)//this means if avx_db>=-10db it will be considered to be voice
	{
		memSpeech[0] = 0;
		if (lasthighdb>Threshold_db && lasthighdb >= -16 && lasthighdb>vippesvdb - 13)
		{
			if (period)
			{
				memSpeech[0] = 1;
			}
		}
	} 
	else
	{

		//if (memSpeech[0] == 0)
		if(memSilenceCountDown[0]<=SPEECH_END_PROTECTION_FOR_USE_P/2)
		{
			if (period)
			{
				memSpeech[0] = 1;
			}
		}
		else
		{
			memSpeech[0] = 1;
		}
	}
	if (memSpeech[0] == 1)
	{
		Speech = 1;
		memSilenceCountDown[0] = SPEECH_END_PROTECTION_FOR_USE_P;
	} 
	else
	{
		if (memSilenceCountDown[0]>0)
		{
			memSilenceCountDown[0]--;
		}
		if (memSilenceCountDown[0]>0)
		{
			//Threshold_db = (((2.2 - 1)/SPEECH_END_PROTECTION_FOR_USE_P)*memSilenceCountDown[0] + 1) * Threshold_db;
			Threshold_db = ((-54-Threshold_db)/SPEECH_END_PROTECTION_FOR_USE_P)*memSilenceCountDown[0] + Threshold_db;

			if(avgstax_db<avgendx_db)
			{
				Threshold_db -= 0.17;

			}
			if(avgstax_db+0.35<avgendx_db)
			{
				Threshold_db -= 0.67;

			}
			//T1 = mAGC->memnoiseVAD_db + 0.15*(TVAD_DB - 0.34);

			if(avgx_db <= Threshold_db||avgx_db <= memnoiseVAD_db - 1.0)
			{
				Speech = 0;
			} 
			else
			{
				Speech = 1;
			}

		} 
		else
		{
			Speech = 0;
		}
	}
	return Speech;
}
int vipVADmod3(int *memSpeech,int *memSilenceCountDown,float memnoiseVAD_db,float avgstax_db,float avgendx_db,float avgx_db,float vippesvdb,float period)
{
	float Threshold_db;
	int Speech;

	if (memSpeech[0] == 1)
	{
		Threshold_db = memnoiseVAD_db + 14;
	} 
	else
	{
		Threshold_db = memnoiseVAD_db + 18;//Threshold_db = mAGC->memnoise_db + T1_DB - 0.3;
	}

	if (memnoiseVAD_db<-42.0)
	{
		//Threshold_db += 2.0;

		if(avgstax_db >= avgendx_db)
		{
			Threshold_db += 0.9;
		} 
		else
		{
			Threshold_db += 0.7;

		}
	}

	if(avgstax_db<avgendx_db)
	{
		Threshold_db -= 0.37;

	}

	Threshold_db = THEMAXOF(Threshold_db,-50.0);//we add -50db noise
	if (Threshold_db>-15)
	{
		Threshold_db = -15;
	}

	if(avgx_db <= Threshold_db || avgx_db < -15.0 || avgx_db < vippesvdb - 11)//this means if avx_db>=-10db it will be considered to be voice
	{
		memSpeech[0] = 0;
	} 
	else
	{
		//if (memSpeech[0] == 0)
		if(memSilenceCountDown[0]<=0)
		{
			if (period>1.2)
			{
				memSpeech[0] = 1;
			}
		}
		else
		{
			memSpeech[0] = 1;
		}
	}
	if (memSpeech[0] == 1)
	{
		Speech = 1;
		memSilenceCountDown[0] = SPEECH_END_PROTECTION;
	} 
	else
	{
		if (memSilenceCountDown[0]>0)
		{
			memSilenceCountDown[0]--;
		}
		if (memSilenceCountDown[0]>0)
		{
			//Threshold_db = (((2.2 - 1)/SPEECH_END_PROTECTION)*memSilenceCountDown[0] + 1) * Threshold_db;
			Threshold_db = ((-54-Threshold_db)/SPEECH_END_PROTECTION)*memSilenceCountDown[0] + Threshold_db;
			if(avgstax_db<avgendx_db)
			{
				Threshold_db -= 0.17;

			}
			if(avgstax_db+0.35<avgendx_db)
			{
				Threshold_db -= 0.67;

			}
			//T1 = mAGC->memnoiseVAD_db + 0.15*(TVAD_DB - 0.34);

			if(avgx_db <= Threshold_db||avgx_db <= memnoiseVAD_db - 1.0)
			{
				Speech = 0;
			} 
			else
			{
				Speech = 1;
			}

		} 
		else
		{
			Speech = 0;
		}
	}
	return Speech;
}
int vipVADmod4(int *memSpeech,int *memSilenceCountDown,float memnoiseVAD_db,float avgstax_db,float avgendx_db,float avgx_db,float vippesvdb,float lasthighdb,float period)
{
	float Threshold_db;
	int Speech;
	float AbsoluteThresholddb;

	if (memSpeech[0] == 1)
	{
		Threshold_db = memnoiseVAD_db + 14;
		AbsoluteThresholddb = -36;//in fact other vad should also be like this
	} 
	else
	{
		Threshold_db = memnoiseVAD_db + 18;//Threshold_db = mAGC->memnoise_db + T1_DB - 0.3;
		AbsoluteThresholddb = -23;
		if (period > 1.6)
		{
			AbsoluteThresholddb = -28;
		}
	}
	if (memnoiseVAD_db<-42.0)
	{
		//Threshold_db += 2.0;

		if(avgstax_db >= avgendx_db)
		{
			Threshold_db += 0.9;
		} 
		else
		{
			Threshold_db += 0.7;

		}
	}

	if(avgstax_db<avgendx_db)
	{
		Threshold_db -= 0.37;

	}

	Threshold_db = THEMAXOF(Threshold_db,-50.0);//we add -50db noise
	if (Threshold_db>-15)
	{
		Threshold_db = -15;
	}

	if(avgx_db <= Threshold_db || avgx_db < AbsoluteThresholddb || avgx_db < vippesvdb - 14)//this means if avx_db>=-10db it will be considered to be voice
	{
		memSpeech[0] = 0;
		if (lasthighdb>Threshold_db && lasthighdb >= -16 && lasthighdb>vippesvdb - 13)
		{
			if (period>1.22)
			{
				memSpeech[0] = 1;
			}
		}
	} 
	else
	{

		//if (memSpeech[0] == 0)
		if(memSilenceCountDown[0]<=SPEECH_END_PROTECTION_FOR_USE_P/2)
		{
			if (period>1.22)
			{
				memSpeech[0] = 1;
			}
		}
		else
		{
			memSpeech[0] = 1;
		}
	}
	if (memSpeech[0] == 1)
	{
		Speech = 1;
		memSilenceCountDown[0] = SPEECH_END_PROTECTION_FOR_USE_P;
	} 
	else
	{
		if (memSilenceCountDown[0]>0)
		{
			memSilenceCountDown[0]--;
		}
		if (memSilenceCountDown[0]>0)
		{
			//Threshold_db = (((2.2 - 1)/SPEECH_END_PROTECTION_FOR_USE_P)*memSilenceCountDown[0] + 1) * Threshold_db;
			Threshold_db = ((-54-Threshold_db)/SPEECH_END_PROTECTION_FOR_USE_P)*memSilenceCountDown[0] + Threshold_db;

			if(avgstax_db<avgendx_db)
			{
				Threshold_db -= 0.17;

			}
			if(avgstax_db+0.35<avgendx_db)
			{
				Threshold_db -= 0.67;

			}
			//T1 = mAGC->memnoiseVAD_db + 0.15*(TVAD_DB - 0.34);

			if(avgx_db <= Threshold_db||avgx_db <= memnoiseVAD_db - 1.0)
			{
				Speech = 0;
			} 
			else
			{
				Speech = 1;
			}

		} 
		else
		{
			Speech = 0;
		}
	}
	return Speech;
}
