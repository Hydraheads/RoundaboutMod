package net.hydra.jojomod.util;


/// THIS IS A TOOL TO MAKE ADDING SOUNDS EASY

import java.util.Objects;
import java.util.Scanner;

class RunnableSoundGenerator {


    public static final String[] strings = {
            "public static final String _&1_ = \"_&2_\"; ",
            "public static final ResourceLocation _&1_ = new ResourceLocation(Roundabout.MOD_ID,_&2_);",
            "public static SoundEvent _&1_ = SoundEvent.createVariableRangeEvent(_&2_);",

            "addSound(ModSounds._&1_, ModSounds._&2_);",

            " public static final RegistryObject<SoundEvent> _&1_ =\n register(ModSounds._&2_, ModSounds._&3_);",

            " \"_&1_\": {\n  \"subtitle\": \"sounds.roundabout._&1_\",\n  \"sounds\": [\n  \"roundabout:_&1_\" \n  ]\n   },"
    };

    public static final String[] string_titles = {"ModSounds","","","FabricSounds","ForgeSounds","sounds.json"};
    public static final String[] string_appends = {"",""," "," "," "," Don't forget to add the lang file!"};


    public static void main(String[] args) {

        boolean running = true;

        while (running) {
            System.out.println("THE NAME OF THE EVENT, WITH UNDERSCORES:");
            Scanner s = new Scanner(System.in);
            String result = s.nextLine().toLowerCase();
            String upper = result.toUpperCase();

            String ID = upper + "_ID";
            String EVENT = upper + "_EVENT";

            String[][] string_replacers = {
                    {upper, result},
                    {ID, upper},
                    {EVENT, ID},
                    {ID, EVENT},
                    {upper, upper, ID},
                    {result}
            };


            for (int i = 0; i < strings.length; i++) {
                String title = string_titles[i];
                if (!Objects.equals(title, "")) {
                    System.out.println(title + ":\n");
                }
                System.out.println(replaceValues(strings[i], string_replacers[i]));
                String append = string_appends[i];
                if (!Objects.equals(append, "")) {
                    System.out.println(append);
                }

            }



        }
        System.out.println("Make sure to:");
        System.out.println("A: add the sound to the en_us lang file");
        System.out.println("B: rename the sound to the id's name");
        System.out.println("C: make sure the sound is .ogg");
        System.out.println("D: make sure the sound is mono (look it up if you're unsure)");






    }

    public static String replaceValues(String str, String[] vargs) {
        for (int i=0;i<vargs.length;i++) {
            String sign = "_&"+(i+1)+"_";
            int index = str.indexOf(sign);
            //System.out.println(sign + " | " + vargs[i]);
            if (index != -1) {
                str = str.replaceAll(sign,vargs[i]);
            }
        }
        return str;
    }

}