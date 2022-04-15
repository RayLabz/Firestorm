//import com.raylabz.firestorm.*;
//import com.raylabz.firestorm.util.FirebaseUtils;
//
//import java.io.IOException;
//import java.util.List;
//
//public class TestGetMany {
//
//    public static void main(String[] args) {
//        String key = "{\n" +
//                "  \"type\": \"service_account\",\n" +
//                "  \"project_id\": \"raylabz\",\n" +
//                "  \"private_key_id\": \"8c175460153dac19a77c7fe4b7e0e9c50ff4814e\",\n" +
//                "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQClMw5P3xSsrQ+D\\nIGBxR1drLTd6uznRkDmHk+EM2KymDRgAeWlNwAGZm3xOKjVo0Uuamq7Qay8pdwsA\\n1n6SwIJgmaT2o7dikqqXwzPB8yv4uKFxJOE6vys21Q0gC7gHN26drFLSAKGsl6/B\\n6Gs58k8B6qEm9CNMXLGLF1v98CUUdwUNfyTKOgtMBJi98xXqmSu+FKK/tv04dOy8\\ncd+lgLBNnjhOTOD7DwSNcO+/ZVpqat0cUosZE0IuywYAUzMVpxSK3D+x/SelIsWI\\ny4EIfzUvxG04oMCvAxpVYx7WPVcyQc8DKupHFVR3Heycz8sAU76ySP2Dr0/iwoff\\nE1cayBNvAgMBAAECggEAEraaX++Cwt67G5z5dSDiOl39uLWTDUtQ9laI5Jt6JI3m\\nJ/juNfmn6hPEEddT2S2gQAFWsn2kctuCemlRyY5CFpmWUhQQltDLafPu8yujJwJv\\nKJxyC7q9CpyOvJBnRnX/3fTietnJy/3GJz6MlZypA4Flw5OzQ0KwH/PuSUaV71/a\\nBnzucktQRwaWWEeI72LYHAUA4cmCC1GMfK4scn/7C2QxvyovKOeSKIdqYlz80R59\\npdIcFSwa9nBlmV7CcbBN0+QXo7KkAiLUTaHDxGCy+88Vs9dIA9VA0Rch71t2C8v5\\nwUA15dKtLQCQcOPpJEEepP1fcKc6Yqu4eLxlIaXEuQKBgQDgBf14dZVHSShEjm2f\\neWh2V4QgA1NIjTaVwdfhA3ZFG9byoYGAnvdpt+yvoiDqBPwfo0JQvYt1NfH4dKBV\\n5NY5iV9WTaAMNkOxRXRLJMwEk3oXwfYpUbSNNwfmBXonRHj9v3icXcZG1IervDBQ\\nKESgpFBDEPG2BUMIDwvIzzz1CQKBgQC8x5ZI+0a89A39VnOfHMm2UbQtAWxuHagb\\ngoksfXY/SLmGlWZVjr2VWQRaoNf7bKVx3lahIblNiWECOFtbYoy+hARAtLHmmzSh\\nD4bdTs6WCazu8NkLqXjCecqKJqcsNQy2tz8LM6oLOgHg5OMFP9tiVAS/jQXUyiHD\\nKowyEUIatwKBgQCPfSa6Fk5UR5aEOtoaUFBJpIqbWqXojZKN34gftvD1GPzXkfz5\\n3lhInab7ZBRC8Ihw10gtoQqHO9NNuZ3NBnL0jXnENJoz2DoJfJwdWWdX8wI731+F\\nXbEQ1lO30IBKUIBSv2ZIeYP7cDuqEzK61OAIKNbD7kFzvfEOY1y/9YVsyQKBgDFA\\n9cIG5XbpSjkd9D7AWRSbjqHrbCnyzOiYj0evWEm8pMMrea669lXgCtXGf3OW7jee\\nZgXWca18OXZF0/gs50HR2fYz7vKW7g1TqsFVDWtyDM+uwUJcrig3dJPE7/sFwFJv\\nzyASE7yoHtNZhK3a8ldeSnrbxXi2YEa2dCuf+xpTAoGBAIaJ4rhOi+7folxTNcpt\\nSpzoWwx77PdqsPH/T3tNqz7RMiY79MBRtPwuBosnlZveUctWUfhzruwUPurGKCuC\\nilPdo96jOzTu2ugCKSFVC8+N2qf3a2lyI0QhLXtaplHVxBHJk551sEOhX50y52kG\\nAB3kWSty9kXZeLANt3o1zvmr\\n-----END PRIVATE KEY-----\\n\",\n" +
//                "  \"client_email\": \"firebase-adminsdk-yk0u2@raylabz.iam.gserviceaccount.com\",\n" +
//                "  \"client_id\": \"115280164110548598123\",\n" +
//                "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
//                "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
//                "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
//                "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-yk0u2%40raylabz.iam.gserviceaccount.com\"\n" +
//                "}\n";
//
//        try {
//            FirebaseUtils.initialize(key);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Firestorm.init();
//        Firestorm.register(Person.class);
////        Firestorm.register(Student.class);
//
////        for (int i = 0; i < 5; i++) {
////            Firestorm.create(new Student("P" + i, i + 10, "Person" + i, 80 + i));
////        }
//
//        //Create your filter first, using the filter() method:
//        FirestormFilterable<Person> filterable = Firestorm.filter(Person.class)
//                .whereGreaterThan("age", 12);
//
//        //Then attach a FilterableListener that uses the filter created above:
//        Firestorm.attachListener(new FilterableListener<Person>(filterable) {
//            @Override
//            public void onSuccess(List<ObjectChange<Person>> objectChanges) {
//                for (ObjectChange<Person> objectChange : objectChanges) {
//                    System.out.println(objectChange.getObject().toString());
//                }
//
//            }
//
//            @Override
//            public void onFailure(String failureMessage) {
//                System.err.println(failureMessage);
//            }
//        });
//
//        while (true) {
//
//        }
//
////        Firestorm.create(new Person("1", 1, "nicos1"), "1");
////        Firestorm.create(new Person("2", 2, "nicos2"), "2");
////        Firestorm.create(new Person("3", 3, "nicos3"), "3");
////        Firestorm.create(new Person("4", 4, "nicos4"), "4");
////
////
////        final List<Person> many = Firestorm.getMany(Person.class, "3", "2");
////        for (Person person : many) {
////            System.out.println(person.getName());
////        }
//
//
//    }
//
//}
