package id.gits.dprkita.utils.json;

import java.util.HashMap;

/**
 * Created by dodulz on 19/02/15.
 */
public class KomisiJSON {
    public static final String KOMISI = "{\"data\":{\"results\":{\"count\":11,\"komisi\":[{\"id\":1,\"nama\":\"I - Pertahanan, Intelijen, Luar Negeri, Komunikasi dan Informatika\"},{\"id\":2,\"nama\":\"II - Pemerintahan Dalam Negeri \\u0026 Otonomi Daerah, Aparatur \\u0026 Reformasi Birokrasi, Kepemiluan, Pertahanan \\u0026 Reforma Agraria\"},{\"id\":3,\"nama\":\"III - Hukum, HAM, Keamanan\"},{\"id\":4,\"nama\":\"IV - Pertanian, Perkebunan, Kehutanan, Kelautan, Perikanan, Pangan\"},{\"id\":5,\"nama\":\"V - Perhubungan, Pekerjaan Umum, Perumahan Rakyat, Pembangunan Pedesaan dan Kawasan Tertinggal, Meteorologi, Klimatologi \\u0026 Geofisika\"},{\"id\":6,\"nama\":\"VI - Perdagangan, Perindustrian, Investasi, Koperasi, UKM \\u0026 BUMN, Stadarisasi Nasional\"},{\"id\":7,\"nama\":\"VII - Energi Sumber Daya Mineral, Riset \\u0026 Teknologi, Lingkungan Hidup\"},{\"id\":8,\"nama\":\"VIII - Agama, Sosial, Pemberdayaan Perempuan\"},{\"id\":9,\"nama\":\"IX - Tenaga Kerja \\u0026 Transmigrasi, Kependudukan, Kesehatan\"},{\"id\":10,\"nama\":\"X - Pendidikan, Kebudayaan, Pariwisata, Ekonomi Kreatif, Pemuda, Olahraga, Perpustakaan\"},{\"id\":11,\"nama\":\"XI - Keuangan, Perencanaan Pembangunan, Perbankan\"}]}}}";

    public static HashMap<Integer, String[]> listRuangLingkup() {
        HashMap<Integer, String[]> data = new HashMap<>();
        data.put(1, new String[]{"Pertahanan", "Luar Negeri", "Informasi"});
        data.put(2, new String[]{"Pemerintahan Dalam Negeri dan Otonomi Daerah", "Aparatur Negara dan Reformasi Birokrasi", "Kepemiluan", "Pertanahan dan Reforma Agraria"});
        data.put(3, new String[]{"Hukum", "HAM", "Keamanan"});
        data.put(4, new String[]{"Pertanian", "Perkebunan", "Kehutanan", "Kelautan", "Perikanan", "Pangan"});
        data.put(5, new String[]{"Perhubungan", "Pekerjaan Umum", "Perumahan Rakyat", "Pembangunan Pedesaan dan Kawasan Tertinggal", "Meteorologi, Klimatologi dan Geofisika"});
        data.put(6, new String[]{"Perdagangan", "Perindustrian", "Investasi", "Koperasi, UKM dan BUMN", "Standarisasi Nasional"});
        data.put(7, new String[]{"Energi Sumber Daya Mineral", "Riset dan Teknologi", "Lingkungan Hidup"});
        data.put(8, new String[]{"Agama", "Sosial", "Pemberdayaan Perempuan"});
        data.put(9, new String[]{"Tenaga Kerja dan Transmigrasi", "Kependudukan", "Kesehatan"});
        data.put(10, new String[]{"Pendidikan", "Pemusa", "Olahraga", "Pariwisata", "Kesenian", "Kebudayaan"});
        data.put(11, new String[]{"Keuangan", "Perencanaan Pembangunan Nasional", "Perbankan", "Lembaga Keuangan Bukan Bank"});
        return data;
    }
}
