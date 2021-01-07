package com.github.zh9131101.utils;

/**
 * <p>
 * 处理GIF图片
 * </p>
 *
 * @author zh9131101
 * @version V1.0.0
 * @date 2021-01-07 22:07
 * @since 1.0
 */

public class NeuQuant {
    /**
     * number of colours used
     */
    protected static final int netsize = 256;

    /* four primes near 500 - assume no image has a length so large */
    /**
     * that it is divisible by all four primes
     */
    protected static final int prime1 = 499;
    protected static final int prime2 = 491;
    protected static final int prime3 = 487;
    protected static final int prime4 = 503;

    protected static final int minpicturebytes = (3 * prime4);
    /* minimum size for input image */

	/* Program Skeleton
	   ----------------
	   [select samplefac in range 1..30]
	   [read image from input file]
	   pic = (unsigned char*) malloc(3*width*height);
	   initnet(pic,3*width*height,samplefac);
	   learn();
	   unbiasnet();
	   [write output image header, using writecolourmap(f)]
	   inxbuild();
	   write output image using inxsearch(b,g,r)      */

	/* Network Definitions
	   ------------------- */

    protected static final int maxnetpos = (netsize - 1);
    /**
     * bias for colour values
     */
    protected static final int netbiasshift = 4;
    /**
     * no. of learning cycles
     */
    protected static final int ncycles = 100;

    /* defs for freq and bias */
    /**
     * bias for fractions
     */
    protected static final int intbiasshift = 16;
    protected static final int intbias = (1 << intbiasshift);
    /**
     * gamma = 1024
     */
    protected static final int gammashift = 10;
    protected static final int gamma = (1 << gammashift);
    protected static final int betashift = 10;
    /**
     * beta = 1/1024
     */
    protected static final int beta = (intbias >> betashift);
    protected static final int betagamma =
            (intbias << (gammashift - betashift));

    /* defs for decreasing radius factor */
    /**
     * for 256 cols, radius starts
     */
    protected static final int initrad = (netsize >> 3);
    /**
     * at 32.0 biased by 6 bits
     */
    protected static final int radiusbiasshift = 6;
    protected static final int radiusbias = (1 << radiusbiasshift);
    /**
     * and decreases by a
     */
    protected static final int initradius = (initrad * radiusbias);
    /**
     * factor of 1/30 each cycle
     */
    protected static final int radiusdec = 30;

    /* defs for decreasing randomChar factor */
    /**
     * randomChar starts at 1.0
     */
    protected static final int alphabiasshift = 10;
    protected static final int initalpha = (1 << alphabiasshift);

    protected int alphadec; /* biased by 10 bits */

    /**
     * radbias and alpharadbias used for radpower calculation
     */
    protected static final int radbiasshift = 8;
    protected static final int radbias = (1 << radbiasshift);
    protected static final int alpharadbshift = (alphabiasshift + radbiasshift);
    protected static final int alpharadbias = (1 << alpharadbshift);

	/* Types and Global Variables
	-------------------------- */

    /**
     * the input image itself
     */
    protected byte[] thepicture;
    /**
     * lengthcount = H*W*3
     */
    protected int lengthcount;
    /**
     * sampling factor 1..30
     */
    protected int samplefac;

    //   typedef int pixel[4];                /* BGRc */
    /**
     * the network itself - [netsize][4]
     */
    protected int[][] network;

    protected int[] netindex = new int[256];
    /* for network lookup - really 256 */

    protected int[] bias = new int[netsize];
    /* bias and freq arrays for learning */
    protected int[] freq = new int[netsize];
    protected int[] radpower = new int[initrad];
    /* radpower for precomputation */

    /* Initialise network in range (0,0,0) to (255,255,255) and set parameters
       ----------------------------------------------------------------------- */

    public NeuQuant(byte[] thepic, int len, int sample) {

        int i;
        int[] p;

        thepicture = thepic;
        lengthcount = len;
        samplefac = sample;

        network = new int[netsize][];
        for (i = 0; i < netsize; i++) {
            network[i] = new int[4];
            p = network[i];
            p[0] = p[1] = p[2] = (i << (netbiasshift + 8)) / netsize;
            //  1/netsize
            freq[i] = intbias / netsize;
            bias[i] = 0;
        }
    }

    public byte[] colorMap() {
        byte[] map = new byte[3 * netsize];
        int[] index = new int[netsize];
        for (int i = 0; i < netsize; i++) {
            index[network[i][3]] = i;
        }
        int k = 0;
        for (int i = 0; i < netsize; i++) {
            int j = index[i];
            map[k++] = (byte) (network[j][0]);
            map[k++] = (byte) (network[j][1]);
            map[k++] = (byte) (network[j][2]);
        }
        return map;
    }

    /* Insertion sort of network and building of netindex[0..255] (to do after unbias)
       ------------------------------------------------------------------------------- */

    public void inxbuild() {

        int i;
        int j;
        int smallpos;
        int smallval;
        int[] p;
        int[] q;
        int previouscol = 0;
        int startpos = 0;
        for (i = 0; i < netsize; i++) {
            p = network[i];
            smallpos = i;
            // index on g
            smallval = p[1];
            /* find smallest in i..netsize-1 */
            for (j = i + 1; j < netsize; j++) {
                q = network[j];
                // index on g
                if (q[1] < smallval) {
                    smallpos = j;
                    //  index on g
                    smallval = q[1];
                }
            }
            q = network[smallpos];
            /* swap p (i) and q (smallpos) entries */
            if (i != smallpos) {
                j = q[0];
                q[0] = p[0];
                p[0] = j;
                j = q[1];
                q[1] = p[1];
                p[1] = j;
                j = q[2];
                q[2] = p[2];
                p[2] = j;
                j = q[3];
                q[3] = p[3];
                p[3] = j;
            }
            /* smallval entry is now in position i */
            if (smallval != previouscol) {
                netindex[previouscol] = (startpos + i) >> 1;
                for (j = previouscol + 1; j < smallval; j++) {
                    netindex[j] = i;
                }
                previouscol = smallval;
                startpos = i;
            }
        }
        netindex[previouscol] = (startpos + maxnetpos) >> 1;
        for (j = previouscol + 1; j < 256; j++) {
            //  really 256
            netindex[j] = maxnetpos;
        }
    }

    /* Main Learning Loop
       ------------------ */

    public void learn() {

        int i;
        int j;
        int b;
        int g;
        int r;
        int radius;
        int rad;
        int alpha;
        int step;
        int delta;
        int samplepixels;
        byte[] p;
        int pix;
        int lim;
        if (lengthcount < minpicturebytes) {
            samplefac = 1;
        }
        alphadec = 30 + ((samplefac - 1) / 3);
        p = thepicture;
        pix = 0;
        lim = lengthcount;
        samplepixels = lengthcount / (3 * samplefac);
        delta = samplepixels / ncycles;
        alpha = initalpha;
        radius = initradius;

        rad = radius >> radiusbiasshift;
        if (rad <= 1) {
            rad = 0;
        }
        for (i = 0; i < rad; i++) {
            radpower[i] = alpha * (((rad * rad - i * i) * radbias) / (rad * rad));
        }
        //fprintf(stderr,"beginning 1D learning: initial radius=%d\n", rad);
        if (lengthcount < minpicturebytes) {
            step = 3;
        } else if ((lengthcount % prime1) != 0) {
            step = 3 * prime1;
        } else {
            if ((lengthcount % prime2) != 0) {
                step = 3 * prime2;
            } else {
                if ((lengthcount % prime3) != 0) {
                    step = 3 * prime3;
                } else {
                    step = 3 * prime4;
                }
            }
        }

        i = 0;
        while (i < samplepixels) {
            b = (p[pix] & 0xff) << netbiasshift;
            g = (p[pix + 1] & 0xff) << netbiasshift;
            r = (p[pix + 2] & 0xff) << netbiasshift;
            j = contest(b, g, r);

            altersingle(alpha, j, b, g, r);
            if (rad != 0) {
                // alter neighbours
                alterneigh(rad, j, b, g, r);
            }
            pix += step;
            if (pix >= lim) {
                pix -= lengthcount;
            }
            i++;
            if (delta == 0) {
                delta = 1;
            }
            if (i % delta == 0) {
                alpha -= alpha / alphadec;
                radius -= radius / radiusdec;
                rad = radius >> radiusbiasshift;
                if (rad <= 1) {
                    rad = 0;
                }
                for (j = 0; j < rad; j++) {
                    radpower[j] = alpha * (((rad * rad - j * j) * radbias) / (rad * rad));
                }
            }
        }
        //fprintf(stderr,"finished 1D learning: final randomChar=%f !\n",((float)randomChar)/initalpha);
    }

    /* Search for BGR values 0..255 (after net is unbiased) and return colour index
       ---------------------------------------------------------------------------- */

    public int map(int b, int g, int r) {

        int i, j, dist, a, bestd;
        int[] p;
        int best;

        // biggest possible dist is 256*3
        bestd = 1000;
        best = -1;
        //  index on g
        i = netindex[g];
        // start at netindex[g] and work outwards
        j = i - 1;

        while ((i < netsize) || (j >= 0)) {
            if (i < netsize) {
                p = network[i];
                //  inx key
                dist = p[1] - g;
                if (dist >= bestd) {
                    //  stop iter
                    i = netsize;
                } else {
                    i++;
                    if (dist < 0) {
                        dist = -dist;
                    }
                    a = p[0] - b;
                    if (a < 0) {
                        a = -a;
                    }
                    dist += a;
                    if (dist < bestd) {
                        a = p[2] - r;
                        if (a < 0) {
                            a = -a;
                        }
                        dist += a;
                        if (dist < bestd) {
                            bestd = dist;
                            best = p[3];
                        }
                    }
                }
            }
            if (j >= 0) {
                p = network[j];
                // inx key - reverse dif
                dist = g - p[1];
                if (dist >= bestd) {
                    //  stop iter
                    j = -1;
                } else {
                    j--;
                    if (dist < 0) {
                        dist = -dist;
                    }
                    a = p[0] - b;
                    if (a < 0) {
                        a = -a;
                    }
                    dist += a;
                    if (dist < bestd) {
                        a = p[2] - r;
                        if (a < 0) {
                            a = -a;
                        }
                        dist += a;
                        if (dist < bestd) {
                            bestd = dist;
                            best = p[3];
                        }
                    }
                }
            }
        }
        return (best);
    }

    public byte[] process() {
        learn();
        unbiasnet();
        inxbuild();
        return colorMap();
    }

    /* Unbias network to give byte values 0..255 and record position i to prepare for sort
       ----------------------------------------------------------------------------------- */

    public void unbiasnet() {

        int i, j;

        for (i = 0; i < netsize; i++) {
            network[i][0] >>= netbiasshift;
            network[i][1] >>= netbiasshift;
            network[i][2] >>= netbiasshift;
            network[i][3] = i; /* record colour no */
        }
    }

    /* Move adjacent neurons by precomputed randomChar*(1-((i-j)^2/[r]^2)) in radpower[|i-j|]
       --------------------------------------------------------------------------------- */

    protected void alterneigh(int rad, int i, int b, int g, int r) {

        int j, k, lo, hi, a, m;
        int[] p;

        lo = i - rad;
        if (lo < -1) {
            lo = -1;
        }
        hi = i + rad;
        if (hi > netsize) {
            hi = netsize;
        }
        j = i + 1;
        k = i - 1;
        m = 1;
        while ((j < hi) || (k > lo)) {
            a = radpower[m++];
            if (j < hi) {
                p = network[j++];
                try {
                    p[0] -= (a * (p[0] - b)) / alpharadbias;
                    p[1] -= (a * (p[1] - g)) / alpharadbias;
                    p[2] -= (a * (p[2] - r)) / alpharadbias;
                } catch (Exception e) {
                } // prevents 1.3 miscompilation
            }
            if (k > lo) {
                p = network[k--];
                try {
                    p[0] -= (a * (p[0] - b)) / alpharadbias;
                    p[1] -= (a * (p[1] - g)) / alpharadbias;
                    p[2] -= (a * (p[2] - r)) / alpharadbias;
                } catch (Exception e) {
                }
            }
        }
    }

    /* Move neuron i towards biased (b,g,r) by factor randomChar
       ---------------------------------------------------- */

    protected void altersingle(int alpha, int i, int b, int g, int r) {

        /* alter hit neuron */
        int[] n = network[i];
        n[0] -= (alpha * (n[0] - b)) / initalpha;
        n[1] -= (alpha * (n[1] - g)) / initalpha;
        n[2] -= (alpha * (n[2] - r)) / initalpha;
    }

    /* Search for biased BGR values
       ---------------------------- */
    protected int contest(int b, int g, int r) {

        /* finds closest neuron (min dist) and updates freq */
        /* finds best neuron (min dist-bias) and returns position */
        /* for frequently chosen neurons, freq[i] is high and bias[i] is negative */
        /* bias[i] = gamma*((1/netsize)-freq[i]) */

        int i, dist, a, biasdist, betafreq;
        int bestpos, bestbiaspos, bestd, bestbiasd;
        int[] n;

        bestd = ~(1 << 31);
        bestbiasd = bestd;
        bestpos = -1;
        bestbiaspos = bestpos;

        for (i = 0; i < netsize; i++) {
            n = network[i];
            dist = n[0] - b;
            if (dist < 0) {
                dist = -dist;
            }
            a = n[1] - g;
            if (a < 0) {
                a = -a;
            }
            dist += a;
            a = n[2] - r;
            if (a < 0) {
                a = -a;
            }
            dist += a;
            if (dist < bestd) {
                bestd = dist;
                bestpos = i;
            }
            biasdist = dist - ((bias[i]) >> (intbiasshift - netbiasshift));
            if (biasdist < bestbiasd) {
                bestbiasd = biasdist;
                bestbiaspos = i;
            }
            betafreq = (freq[i] >> betashift);
            freq[i] -= betafreq;
            bias[i] += (betafreq << gammashift);
        }
        freq[bestpos] += beta;
        bias[bestpos] -= betagamma;
        return (bestbiaspos);
    }
}

