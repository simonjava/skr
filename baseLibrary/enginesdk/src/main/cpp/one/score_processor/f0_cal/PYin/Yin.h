/* -*- c-basic-offset: 4 indent-tabs-mode: nil -*-  vi:set ts=8 sts=4 sw=4: */

/*
    pYIN - A fundamental frequency estimator for monophonic audio
    Centre for Digital Music, Queen Mary, University of London.
    
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License as
    published by the Free Software Foundation; either version 2 of the
    License, or (at your option) any later version.  See the file
    COPYING included with this distribution for more information.
*/

#ifndef _YIN_H_
#define _YIN_H_

#include <cmath>

#include <iostream>
#include <vector>
#include <exception>


//#include "vamp-sdk/FFT.h"
//#include "MeanFilter.h"
#include "YinUtil.h"

class Yin
{
public:
    Yin(size_t frameSize, size_t inputSampleRate, double thresh = 0.2, bool fast = true);
    virtual ~Yin();

    struct YinOutput {
        double f0;
        double periodicity;
        double rms;
        std::vector<double> salience;
        std::vector<std::pair<double, double> > freqProb;
        YinOutput() :  f0(0), periodicity(0), rms(0), 
            salience(std::vector<double>(0)), freqProb(std::vector<std::pair<double, double> >(0)) { }
        YinOutput(double _f, double _p, double _r) :
            f0(_f), periodicity(_p), rms(_r), 
            salience(std::vector<double>(0)), freqProb(std::vector<std::pair<double, double> >(0)) { }
        YinOutput(double _f, double _p, double _r, std::vector<double> _salience) :
            f0(_f), periodicity(_p), rms(_r), salience(_salience), 
            freqProb(std::vector<std::pair<double, double> >(0)) { }
    };
    
    int setThreshold(double parameter);
    int setThresholdDistr(float parameter);
    int setFrameSize(size_t frameSize);
    int setFast(bool fast);
    // int setRemoveUnvoiced(bool frameSize);
    YinOutput process(const double *in) ;
    YinOutput processProbabilisticYin(const double *in) ;

private:
    mutable size_t m_frameSize;
    mutable size_t m_inputSampleRate;
    mutable double m_thresh;
    mutable size_t m_threshDistr;
    mutable size_t m_yinBufferSize;
    mutable bool   m_fast;
    YinUtil m_yinUtil;
    double* m_yinBuffer=NULL;
    float * m_peakProbability=NULL;
    // mutable bool m_removeUnvoiced;
};

#endif
