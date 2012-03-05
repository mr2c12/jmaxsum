/*
 *  Copyright (C) 2012 Michele Roncalli <roncallim at gmail dot com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package system;

import exception.InitializatedException;
import exception.ParameterNotFoundException;
import exception.UnInitializatedException;
import exception.VariableNotSetException;
import factorgraph.NodeVariable;
import java.io.FileNotFoundException;
import java.io.IOException;
import misc.Utils;
import olimpo.Eris;
import powerGrid.PowerGrid;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class SARecycler {

    final static int debug = test.DebugVerbosity.debugSARecycler;



    public SARecycler(int numberOfExecutions, int iteration, COP_Instance cop, String report, String type, double tmax) {

        try {

            double finalValue = Double.POSITIVE_INFINITY;
            int mink = 0;
            Eris eris = new Eris("min", cop,type);
            eris.setIterationsNumber(iteration);
            eris.setTemperature(tmax);
            long start = System.currentTimeMillis();
            for (int i = 0; i < numberOfExecutions; i++) {



                if (debug >= 3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "Iteration " + (i + 1));
                    System.out.println("---------------------------------------");
                }
                eris.solve();
                if (debug>=3) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "cop actual value: "+cop.actualValue());
                        System.out.println("---------------------------------------");
                }
                if (cop.actualValue() < finalValue) {
                    finalValue = cop.actualValue();
                    mink = eris.minK();
                }
                if (debug >= 3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "Iteration " + (i + 1) + " concluded with final=" + finalValue + " and k=" + mink);
                                    if (type.equalsIgnoreCase("noinf")){
                    double finalRelaxed = ((RelaxableCop_Instance)cop).actualRelaxedValue();
                    System.out.println("relaxed=" + finalRelaxed + "\n");
                }
                    System.out.println("---------------------------------------");
                }


            }

            /*
             * final=475.6957480863492;NodeVariable_561=188;NodeVariable_560=187;NodeVariable_462=182;NodeVariable_461=7;NodeVariable_460=170;NodeVariable_469=157;NodeVariable_467=156;NodeVariable_468=164;NodeVariable_465=192;NodeVariable_466=156;NodeVariable_463=155;NodeVariable_464=155;NodeVariable_564=99;NodeVariable_565=189;NodeVariable_562=147;NodeVariable_563=196;NodeVariable_568=190;NodeVariable_569=190;NodeVariable_566=189;NodeVariable_567=72;NodeVariable_550=184;NodeVariable_451=87;NodeVariable_450=151;NodeVariable_456=153;NodeVariable_559=187;NodeVariable_457=15;NodeVariable_458=153;NodeVariable_459=154;NodeVariable_452=151;NodeVariable_453=111;NodeVariable_454=152;NodeVariable_455=152;NodeVariable_551=100;NodeVariable_552=185;NodeVariable_553=102;NodeVariable_554=185;NodeVariable_555=186;NodeVariable_556=186;NodeVariable_557=186;NodeVariable_558=187;NodeVariable_583=195;NodeVariable_582=31;NodeVariable_581=194;NodeVariable_580=89;NodeVariable_440=147;NodeVariable_443=119;NodeVariable_444=149;NodeVariable_441=148;NodeVariable_442=28;NodeVariable_447=150;NodeVariable_448=150;NodeVariable_445=149;NodeVariable_446=149;NodeVariable_449=150;NodeVariable_588=197;NodeVariable_589=197;NodeVariable_586=128;NodeVariable_587=196;NodeVariable_584=195;NodeVariable_585=196;NodeVariable_194=65;NodeVariable_570=65;NodeVariable_193=185;NodeVariable_192=181;NodeVariable_572=191;NodeVariable_191=64;NodeVariable_571=191;NodeVariable_198=19;NodeVariable_197=66;NodeVariable_196=134;NodeVariable_195=66;NodeVariable_190=64;NodeVariable_430=26;NodeVariable_431=79;NodeVariable_432=145;NodeVariable_433=145;NodeVariable_434=115;NodeVariable_435=151;NodeVariable_436=146;NodeVariable_437=146;NodeVariable_438=147;NodeVariable_577=27;NodeVariable_439=42;NodeVariable_578=193;NodeVariable_199=120;NodeVariable_579=194;NodeVariable_573=192;NodeVariable_574=192;NodeVariable_575=173;NodeVariable_576=193;NodeVariable_521=174;NodeVariable_520=24;NodeVariable_525=176;NodeVariable_429=144;NodeVariable_524=84;NodeVariable_428=189;NodeVariable_523=175;NodeVariable_427=153;NodeVariable_522=152;NodeVariable_426=143;NodeVariable_529=177;NodeVariable_425=142;NodeVariable_528=177;NodeVariable_424=148;NodeVariable_527=176;NodeVariable_423=142;NodeVariable_526=10;NodeVariable_422=141;NodeVariable_421=141;NodeVariable_420=20;NodeVariable_510=171;NodeVariable_417=140;NodeVariable_512=171;NodeVariable_416=139;NodeVariable_511=137;NodeVariable_419=140;NodeVariable_514=172;NodeVariable_418=140;NodeVariable_513=90;NodeVariable_413=13;NodeVariable_516=173;NodeVariable_412=138;NodeVariable_515=172;NodeVariable_415=139;NodeVariable_518=103;NodeVariable_414=73;NodeVariable_517=173;NodeVariable_519=174;NodeVariable_411=138;NodeVariable_410=137;NodeVariable_547=183;NodeVariable_408=137;NodeVariable_546=8;NodeVariable_407=136;NodeVariable_545=182;NodeVariable_406=199;NodeVariable_544=18;NodeVariable_405=30;NodeVariable_543=40;NodeVariable_542=181;NodeVariable_541=35;NodeVariable_540=181;NodeVariable_409=183;NodeVariable_400=167;NodeVariable_404=135;NodeVariable_403=171;NodeVariable_549=184;NodeVariable_402=135;NodeVariable_548=183;NodeVariable_401=144;NodeVariable_534=179;NodeVariable_533=178;NodeVariable_536=179;NodeVariable_535=97;NodeVariable_530=177;NodeVariable_532=178;NodeVariable_531=178;NodeVariable_538=163;NodeVariable_537=180;NodeVariable_539=180;NodeVariable_91=25;NodeVariable_90=31;NodeVariable_93=32;NodeVariable_92=31;NodeVariable_95=136;NodeVariable_94=32;NodeVariable_97=33;NodeVariable_96=53;NodeVariable_87=114;NodeVariable_88=29;NodeVariable_89=30;NodeVariable_82=49;NodeVariable_81=106;NodeVariable_80=27;NodeVariable_86=29;NodeVariable_85=199;NodeVariable_84=94;NodeVariable_83=28;NodeVariable_78=27;NodeVariable_79=184;NodeVariable_76=26;NodeVariable_77=43;NodeVariable_98=33;NodeVariable_99=88;NodeVariable_598=121;NodeVariable_597=200;NodeVariable_596=199;NodeVariable_595=126;NodeVariable_599=200;NodeVariable_499=167;NodeVariable_498=171;NodeVariable_497=166;NodeVariable_496=30;NodeVariable_494=75;NodeVariable_495=166;NodeVariable_492=165;NodeVariable_493=165;NodeVariable_490=164;NodeVariable_491=17;NodeVariable_590=175;NodeVariable_593=198;NodeVariable_594=124;NodeVariable_591=78;NodeVariable_592=198;NodeVariable_486=163;NodeVariable_485=138;NodeVariable_488=163;NodeVariable_487=163;NodeVariable_489=164;NodeVariable_480=161;NodeVariable_481=161;NodeVariable_482=24;NodeVariable_483=162;NodeVariable_484=69;NodeVariable_477=190;NodeVariable_476=59;NodeVariable_475=159;NodeVariable_474=159;NodeVariable_479=160;NodeVariable_478=160;NodeVariable_472=158;NodeVariable_473=80;NodeVariable_470=166;NodeVariable_471=158;NodeVariable_10=4;NodeVariable_334=175;NodeVariable_11=6;NodeVariable_333=112;NodeVariable_12=5;NodeVariable_332=32;NodeVariable_13=20;NodeVariable_331=48;NodeVariable_14=5;NodeVariable_338=113;NodeVariable_15=6;NodeVariable_337=113;NodeVariable_16=134;NodeVariable_336=182;NodeVariable_17=6;NodeVariable_335=112;NodeVariable_18=7;NodeVariable_19=189;NodeVariable_339=114;NodeVariable_330=111;NodeVariable_20=62;NodeVariable_9=4;NodeVariable_343=115;NodeVariable_23=8;NodeVariable_342=115;NodeVariable_24=9;NodeVariable_21=8;NodeVariable_345=116;NodeVariable_22=146;NodeVariable_344=131;NodeVariable_208=70;NodeVariable_347=116;NodeVariable_27=10;NodeVariable_207=70;NodeVariable_346=29;NodeVariable_28=10;NodeVariable_25=9;NodeVariable_349=34;NodeVariable_209=70;NodeVariable_26=190;NodeVariable_348=117;NodeVariable_1=1;NodeVariable_204=69;NodeVariable_2=7;NodeVariable_203=125;NodeVariable_29=38;NodeVariable_3=58;NodeVariable_206=167;NodeVariable_4=2;NodeVariable_205=69;NodeVariable_5=2;NodeVariable_200=67;NodeVariable_6=3;NodeVariable_7=3;NodeVariable_202=68;NodeVariable_8=3;NodeVariable_201=68;NodeVariable_0=1;NodeVariable_31=11;NodeVariable_30=11;NodeVariable_340=143;NodeVariable_341=114;NodeVariable_316=106;NodeVariable_315=122;NodeVariable_314=105;NodeVariable_219=74;NodeVariable_313=105;NodeVariable_218=73;NodeVariable_312=44;NodeVariable_311=36;NodeVariable_310=104;NodeVariable_213=72;NodeVariable_212=71;NodeVariable_211=98;NodeVariable_210=71;NodeVariable_217=178;NodeVariable_216=105;NodeVariable_319=107;NodeVariable_215=111;NodeVariable_318=107;NodeVariable_214=72;NodeVariable_317=106;NodeVariable_325=109;NodeVariable_229=77;NodeVariable_324=109;NodeVariable_327=110;NodeVariable_326=66;NodeVariable_321=160;NodeVariable_320=130;NodeVariable_323=108;NodeVariable_322=108;NodeVariable_222=75;NodeVariable_221=74;NodeVariable_224=136;NodeVariable_223=75;NodeVariable_226=76;NodeVariable_329=110;NodeVariable_225=76;NodeVariable_328=125;NodeVariable_228=77;NodeVariable_227=76;NodeVariable_220=74;NodeVariable_55=19;NodeVariable_54=19;NodeVariable_57=20;NodeVariable_56=54;NodeVariable_59=113;NodeVariable_58=3;NodeVariable_60=75;NodeVariable_61=21;NodeVariable_62=21;NodeVariable_63=22;NodeVariable_64=22;NodeVariable_306=103;NodeVariable_307=103;NodeVariable_308=103;NodeVariable_309=28;NodeVariable_68=23;NodeVariable_67=23;NodeVariable_66=144;NodeVariable_300=101;NodeVariable_65=64;NodeVariable_301=101;NodeVariable_302=158;NodeVariable_303=102;NodeVariable_304=102;NodeVariable_69=24;NodeVariable_305=102;NodeVariable_101=34;NodeVariable_70=104;NodeVariable_100=34;NodeVariable_71=145;NodeVariable_103=35;NodeVariable_102=35;NodeVariable_105=36;NodeVariable_74=25;NodeVariable_104=124;NodeVariable_75=26;NodeVariable_107=9;NodeVariable_72=25;NodeVariable_106=36;NodeVariable_73=132;NodeVariable_109=37;NodeVariable_108=37;NodeVariable_37=13;NodeVariable_36=13;NodeVariable_39=156;NodeVariable_38=51;NodeVariable_33=12;NodeVariable_32=11;NodeVariable_35=165;NodeVariable_34=12;NodeVariable_40=14;NodeVariable_41=14;NodeVariable_42=15;NodeVariable_49=17;NodeVariable_48=17;NodeVariable_47=16;NodeVariable_46=16;NodeVariable_45=89;NodeVariable_44=54;NodeVariable_43=15;NodeVariable_52=18;NodeVariable_53=18;NodeVariable_50=98;NodeVariable_51=18;NodeVariable_272=148;NodeVariable_273=92;NodeVariable_274=92;NodeVariable_275=92;NodeVariable_270=91;NodeVariable_271=68;NodeVariable_133=86;NodeVariable_134=45;NodeVariable_135=46;NodeVariable_136=123;NodeVariable_137=145;NodeVariable_138=161;NodeVariable_139=60;NodeVariable_130=44;NodeVariable_132=45;NodeVariable_131=44;NodeVariable_277=150;NodeVariable_276=93;NodeVariable_279=94;NodeVariable_278=93;NodeVariable_285=96;NodeVariable_286=96;NodeVariable_283=95;NodeVariable_284=95;NodeVariable_500=106;NodeVariable_281=191;NodeVariable_501=168;NodeVariable_282=95;NodeVariable_502=168;NodeVariable_503=168;NodeVariable_280=94;NodeVariable_146=87;NodeVariable_504=169;NodeVariable_147=50;NodeVariable_505=169;NodeVariable_144=49;NodeVariable_506=169;NodeVariable_145=81;NodeVariable_507=170;NodeVariable_508=170;NodeVariable_509=170;NodeVariable_148=193;NodeVariable_149=104;NodeVariable_143=48;NodeVariable_142=48;NodeVariable_141=67;NodeVariable_140=47;NodeVariable_289=97;NodeVariable_288=97;NodeVariable_287=96;NodeVariable_290=44;NodeVariable_291=98;NodeVariable_292=5;NodeVariable_293=93;NodeVariable_294=99;NodeVariable_119=40;NodeVariable_295=99;NodeVariable_296=46;NodeVariable_297=183;NodeVariable_393=39;NodeVariable_115=38;NodeVariable_394=132;NodeVariable_116=39;NodeVariable_395=132;NodeVariable_117=40;NodeVariable_396=117;NodeVariable_118=108;NodeVariable_111=142;NodeVariable_390=131;NodeVariable_112=38;NodeVariable_391=131;NodeVariable_113=50;NodeVariable_392=4;NodeVariable_114=39;NodeVariable_110=37;NodeVariable_398=133;NodeVariable_397=133;NodeVariable_399=134;NodeVariable_299=100;NodeVariable_298=100;NodeVariable_128=43;NodeVariable_129=82;NodeVariable_126=43;NodeVariable_127=41;NodeVariable_124=42;NodeVariable_125=42;NodeVariable_122=70;NodeVariable_123=157;NodeVariable_121=198;NodeVariable_120=41;NodeVariable_178=60;NodeVariable_177=14;NodeVariable_370=172;NodeVariable_179=60;NodeVariable_372=151;NodeVariable_371=21;NodeVariable_374=2;NodeVariable_373=125;NodeVariable_231=141;NodeVariable_230=109;NodeVariable_236=79;NodeVariable_237=80;NodeVariable_238=80;NodeVariable_239=200;NodeVariable_232=78;NodeVariable_233=78;NodeVariable_234=23;NodeVariable_235=79;NodeVariable_375=126;NodeVariable_170=73;NodeVariable_376=126;NodeVariable_171=58;NodeVariable_377=126;NodeVariable_172=12;NodeVariable_378=127;NodeVariable_173=58;NodeVariable_379=127;NodeVariable_174=59;NodeVariable_175=59;NodeVariable_176=59;NodeVariable_381=128;NodeVariable_380=127;NodeVariable_189=118;NodeVariable_188=63;NodeVariable_385=67;NodeVariable_384=129;NodeVariable_383=45;NodeVariable_382=128;NodeVariable_242=81;NodeVariable_241=159;NodeVariable_240=81;NodeVariable_249=84;NodeVariable_247=195;NodeVariable_248=83;NodeVariable_245=82;NodeVariable_246=83;NodeVariable_243=82;NodeVariable_244=197;NodeVariable_182=61;NodeVariable_388=130;NodeVariable_183=62;NodeVariable_389=129;NodeVariable_180=61;NodeVariable_386=129;NodeVariable_181=25;NodeVariable_387=130;NodeVariable_186=46;NodeVariable_187=174;NodeVariable_184=16;NodeVariable_185=135;NodeVariable_350=117;NodeVariable_159=54;NodeVariable_352=118;NodeVariable_351=118;NodeVariable_156=53;NodeVariable_155=52;NodeVariable_158=53;NodeVariable_157=155;NodeVariable_251=77;NodeVariable_250=84;NodeVariable_253=85;NodeVariable_252=180;NodeVariable_254=116;NodeVariable_255=107;NodeVariable_256=86;NodeVariable_257=86;NodeVariable_258=62;NodeVariable_259=87;NodeVariable_151=51;NodeVariable_357=120;NodeVariable_152=71;NodeVariable_358=26;NodeVariable_153=52;NodeVariable_359=120;NodeVariable_154=133;NodeVariable_353=139;NodeVariable_354=119;NodeVariable_355=64;NodeVariable_150=51;NodeVariable_356=119;NodeVariable_363=122;NodeVariable_362=121;NodeVariable_361=121;NodeVariable_360=50;NodeVariable_169=57;NodeVariable_168=57;NodeVariable_167=56;NodeVariable_166=56;NodeVariable_260=87;NodeVariable_264=89;NodeVariable_263=57;NodeVariable_262=88;NodeVariable_261=88;NodeVariable_267=90;NodeVariable_268=90;NodeVariable_265=47;NodeVariable_266=89;NodeVariable_269=176;NodeVariable_164=55;NodeVariable_165=56;NodeVariable_162=55;NodeVariable_368=123;NodeVariable_163=55;NodeVariable_369=124;NodeVariable_160=52;NodeVariable_366=123;NodeVariable_161=1;NodeVariable_367=68;NodeVariable_364=122;NodeVariable_365=61;
                total time [ms]=3468
                latest value got at iteration=31
             */
            long finish = System.currentTimeMillis();

            if (report == null){


                System.out.println("final="+finalValue+";");
                System.out.println("Conclusion for original cop="+finalValue+";");
                System.out.println("total time [ms]="+(finish-start));
                System.out.println("latest value got at iteration="+mink);
            }
            else {
                StringBuilder string = new StringBuilder();
                string.append("final=").append(finalValue).append(";\n" + "total time [ms]=").append(finish - start).append("\nlatest value got at iteration=").append(mink);
                Utils.stringToFile(string.toString(), report);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (VariableNotSetException ex) {
            ex.printStackTrace();
        } catch (ParameterNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

        try {
            System.out.println("If args are given: FILEPATH REPETITION ITERATION TEMPERATURE");
            String filepath = "/home/mik/Documenti/univr/Ragionamento Automatico/stage/report/200/0.29/2.pg";
            if (args.length >=1){
                filepath = args[0];
            }
            PowerGrid pg = new PowerGrid(filepath);
            //PowerGrid pg = new PowerGrid(2, 3, 2, 0.2, 0.1);

            COP_Instance original_cop = pg.getCopMnotInfNoCo2();
            if (args.length >= 4){
                SARecycler sar = new SARecycler(
                        Integer.parseInt(args[1]),
                        Integer.parseInt(args[2]),
                        original_cop, null, "startandstop",
                        Integer.parseInt(args[3]));
            }
            else {
                SARecycler sar = new SARecycler(1, 25000000, original_cop, null, "startandstop",1000);
            }
        } catch (UnInitializatedException ex) {
            ex.printStackTrace();
        } catch (InitializatedException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
