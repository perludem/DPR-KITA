package id.gits.dprkita.api;

import id.gits.dprkita.dao.BaseDao;
import id.gits.dprkita.dao.TOSDao;
import id.gits.dprkita.dao.anggota.DataAnggota;
import id.gits.dprkita.dao.geographic.DataGeo;
import id.gits.dprkita.dao.jetpackdao.JetPackComments;
import id.gits.dprkita.dao.jetpackdao.JetPackPosts;
import id.gits.dprkita.dao.jetpackdao.JetPackPostsPage;
import id.gits.dprkita.dao.komisi.DataKomisi;
import id.gits.dprkita.dao.news.NewsDao;
import id.gits.dprkita.dao.partai.DataPartai;
import id.gits.dprkita.dao.selasar.DataSelasar;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by yatnosudar on 1/28/15.
 */

public interface ApiInterface {


    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/candidate/api/dapil")
    void listDapil(@Query("apiKey") String apiKey, @Query("lembaga") String lembaga, Callback<BaseDao<id.gits.dprkita.dao.dapil.Data>> data);

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/dapil/api/dapil")
    void listDapil_v2(@Query("apiKey") String apiKey, @Query("limit") int limit, Callback<BaseDao<id.gits.dprkita.dao.dapil.Data>> data);

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/candidate/api/provinsi")
    void listProvinsi(@Query("apiKey") String apiKey, Callback<BaseDao<id.gits.dprkita.dao.provinsi.Data>> data);

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/berita")
    void listBerita(@Query("json") String typeJson, @Query("apiKey") String apiKey, @Query("page") int page, Callback<NewsDao> data);

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/infografis_selasar/api/infografis")
    void listSelasar(@Query("apiKey") String apiKey,@Query("offset") int offset, Callback<BaseDao<DataSelasar>> data);

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/dpr/api/anggota")
    void listAnggotaByPartai(@Query("apiKey") String apiKey, @Query("partai") int partai, Callback<BaseDao<DataAnggota>> data);

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/dpr/api/anggota")
    void listAnggotaByName(@Query("apiKey") String apiKey, @Query("nama") String nama, Callback<BaseDao<DataAnggota>> data);

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/dpr/api/anggota")
    void listAnggotaByKomisi(@Query("apiKey") String apiKey, @Query("komisi") int partai, Callback<BaseDao<DataAnggota>> data);

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/dpr/api/komisi")
    void listkomisi(@Query("apiKey") String apiKey, Callback<BaseDao<DataKomisi>> data);

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/dpr/api/anggota/{user}")
    void detailAnggota(@Path("user") String user, @Query("apiKey") String apiKey, Callback<BaseDao<DataAnggota>> data);

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/geographic/api/point")
    void getDapilPosition(@Query("apiKey") String apiKey, @Query("lat") String lat, @Query("long") String lng, Callback<BaseDao<DataGeo>> data);

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/dpr/api/anggota")
    void listAnggotaByDapil(@Query("apiKey") String apiKey, @Query("dapil") String dapil, Callback<BaseDao<DataAnggota>> data);

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/candidate/api/partai")
    void listPartai(@Query("apiKey") String apiKey, Callback<BaseDao<DataPartai>> callback);

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/posts/")
    void listPostJetPack(@Query("type") String type, @Query("meta_key") String meta_key, @Query("meta_value") String meta_value, @Query("page") int page, Callback<JetPackPosts> data);

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/posts/")
    void listPostJetPack(@Query("type") String type, @Query("meta_key") String meta_key, @Query("meta_value") String meta_value, @Query("author") String author, Callback<JetPackPosts> data);

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/posts/")
    void listPostJetPack(@Query("type") String type, @Query("meta_key") String meta_key, @Query("meta_value") String meta_value, @Query("order") String order, @Query("order_by") String order_by, Callback<JetPackPosts> data);

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/posts/")
    void listPostJetPack(@Query("category") String category, @Query("page") int page, @Query("number") int limit, Callback<JetPackPostsPage> data);

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/posts/")
    void listPostJetPackSearch(@Query("type") String type, @Query("search") String search,@Query("page") int page, Callback<JetPackPosts> data);

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/posts/slug:{slug}")
    void detailPostJetPack(@Path("slug") String slug, Callback<TOSDao> data);

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=10000000")
    @GET("/posts/{id}/replies/")
    void getCommentJetPack(@Path("id") int id, @Query("page") int page, Callback<JetPackComments> data);


}
