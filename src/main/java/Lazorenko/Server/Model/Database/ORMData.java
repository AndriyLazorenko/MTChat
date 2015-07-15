//package Lazorenko.Server.Model.Database;
//
///**
// * Created by andriylazorenko on 26.06.15.
// */
//
//@Entity
//@Table (name = "users")
//public class ORMData {
//    public ORMData() {
//    }
//
//    public ORMData(long id,String name, String email, String password) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.password = password;
//        this.defeats=0;
//        this.victories=0;
//        this.draws=0;
//        this.totalGamesPlayed=0;
//    }
//
//    private long id;
//    @Column (name = "username",length = 30)
//    private String name;
//    @Column (name = "email", nullable = false, unique = true)
//    private String email;
//    @Column (nullable = false)
//    private String password;
//    private int rating;
//    @Column (name = "victories")
//    private int victories;
//    @Column (name = "defeats")
//    private int defeats;
//    @Column (name = "draws")
//    private int draws;
//    @Column (name = "total games played")
//    private int totalGamesPlayed;
//
//}
