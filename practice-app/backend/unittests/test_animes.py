import unittest
import random
import string
import sqlite3
import os

from flask.wrappers import Response
from main import main
from main.helpers import anime_helper
from main.db.mapper import create_anime_mapper
from HtmlTestRunner import HTMLTestRunner

class TestAnimeEndpoint(unittest.TestCase):

    ###############UTILITY METHODS###############
    def randomString(self, length):
        return ''.join(random.choice(string.ascii_letters) for i in range(length))

    def randomAnimeGenerator(self):
        return {
            "title": self.randomString(10),
            "episodes": random.randint(0, 20),
            "image": "image_url",
            "airing": False,
            "start_date": "27/01/1999",
            "end_date": None,
            "score": round(random.uniform(0, 10), 1),
            "rating": "sR",
            "type": self.randomString(3),
            "synopsis": "synopsis"
        }

    ###############SAMPLE DATA###############
    get_data = [
        {
            "title": "JoJo no Kimyou na Bouken (TV)",
            "mal_id": 14719,
            "episodes": 26,
            "image": "https://cdn.myanimelist.net/images/anime/3/40409.jpg",
            "airing": False,
            "start_date": "2012-10-06T00:00:00+00:00",
            "end_date": "2013-04-06T00:00:00+00:00",
            "score": 7.99,
            "rating": "R - 17+ (violence & profanity)",
            "type": "TV",
            "synopsis": "In 1868, Dario Brando saves the life of an English nobleman, George Joestar. By taking in Dario's son Dio when the boy becomes fatherless, George hopes to repay the debt he owes to his savior. However Dio, unsatisfied with his station in life, aspires to seize the Joestar house for his own. Wielding an Aztec stone mask with supernatural properties, he sets out to destroy George and his son, Jonathan \"JoJo\" Joestar, and triggers a chain of events that will continue to echo through the years to come. Half a century later, in New York City, Jonathan's grandson Joseph Joestar discovers the legacy his grandfather left for him. When an archeological dig unearths the truth behind the stone mask, he realizes that he is the only one who can defeat the Pillar Men, mystical beings of immeasurable power who inadvertently began everything. Adapted from the first two arcs of Hirohiko Araki's outlandish manga series, JoJo no Kimyou na Bouken follows the many thrilling expeditions of JoJo and his descendants. Whether it's facing off with the evil Dio, or combatting the sinister Pillar Men, there's always plenty of bizarre adventures in store. [Written by MAL Rewrite]",
            "duration": 24,
            "sequel": [
                "JoJo no Kimyou na Bouken Part 3: Stardust Crusaders",
                20899
            ],
            "prequel": None,
            "genres": [
                "Action",
                "Adventure",
                "Supernatural",
                "Vampire",
                "Shounen"
            ]
        },
        {
            "title": "Hunter x Hunter (2011)",
            "mal_id": 11061,
            "episodes": 148,
            "image": "https://cdn.myanimelist.net/images/anime/11/33657.jpg",
            "airing": False,
            "start_date": "2011-10-02T00:00:00+00:00",
            "end_date": "2014-09-24T00:00:00+00:00",
            "score": 9.07,
            "rating": "PG-13 - Teens 13 or older",
            "type": "TV",
            "synopsis": "Hunter x Hunter is set in a world where Hunters exist to perform all manner of dangerous tasks like capturing criminals and bravely searching for lost treasures in uncharted territories. Twelve-year-old Gon Freecss is determined to become the best Hunter possible in hopes of finding his father, who was a Hunter himself and had long ago abandoned his young son. However, Gon soon realizes the path to achieving his goals is far more challenging than he could have ever imagined. Along the way to becoming an official Hunter, Gon befriends the lively doctor-in-training Leorio, vengeful Kurapika, and rebellious ex-assassin Killua. To attain their own goals and desires, together the four of them take the Hunter Exam, notorious for its low success rate and high probability of death. Throughout their journey, Gon and his friends embark on an adventure that puts them through many hardships and struggles. They will meet a plethora of monsters, creatures, and characters—all while learning what being a Hunter truly means. [Written by MAL Rewrite]",
            "duration": 23,
            "sequel": None,
            "prequel": None,
            "genres": [
                "Action",
                "Adventure",
                "Fantasy",
                "Shounen",
                "Super Power"
            ]
        }
    ]

    search_query = "jojo"
    seach_limit = "5"

    search_data = [
        {
            "title": "JoJo no Kimyou na Bouken",
            "image": "https://cdn.myanimelist.net/images/anime/1171/106036.jpg?s=09307fb4b030b5e6f578c6ba6cbf84ab",
            "synopsis": "Kujo Jotaro is a normal, popular Japanese high-schooler, until he thinks that he is possessed by a spirit, and locks himself in prison. After seeing his grandfather, Joseph Joestar, and fighting Josep...",
            "type": "OVA",
            "start_date": "1993-11-19T00:00:00+00:00",
            "end_date": "1994-11-18T00:00:00+00:00",
            "score": 7.35,
            "rating": "R",
            "airing": False,
            "mal_id": 666
        },
        {
            "title": "JoJo no Kimyou na Bouken (TV)",
            "image": "https://cdn.myanimelist.net/images/anime/3/40409.jpg?s=eade1f76434383f5af5243cc52d50316",
            "synopsis": "In 1868, Dario Brando saves the life of an English nobleman, George Joestar. By taking in Dario's son Dio when the boy becomes fatherless, George hopes to repay the debt he owes to his savior. However...",
            "type": "TV",
            "start_date": "2012-10-06T00:00:00+00:00",
            "end_date": "2013-04-06T00:00:00+00:00",
            "score": 7.99,
            "rating": "R",
            "airing": False,
            "mal_id": 14719
        },
        {
            "title": "JoJo no Kimyou na Bouken: Adventure",
            "image": "https://cdn.myanimelist.net/images/anime/8/53147.jpg?s=37ab16460192a2614f4bb8ea55f948f5",
            "synopsis": "Kujo Jotaro is a normal, popular Japanese high-schooler, until he thinks that he is possessed by a spirit, and locks himself in prison. After seeing his grandfather, Joseph Joestar, and fighting Jose...",
            "type": "OVA",
            "start_date": "2000-05-25T00:00:00+00:00",
            "end_date": "2002-01-25T00:00:00+00:00",
            "score": 7.19,
            "rating": "R+",
            "airing": False,
            "mal_id": 665
        },
        {
            "title": "JoJo no Kimyou na Bouken Part 5: Ougon no Kaze",
            "image": "https://cdn.myanimelist.net/images/anime/1572/95010.jpg?s=88db8e76960fce4e7aaa69f71cfbb4cb",
            "synopsis": "In the coastal city of Naples, corruption is teeming—the police blatantly conspire with outlaws, drugs run rampant around the youth, and the mafia governs the streets with an iron fist. However, vario...",
            "type": "TV",
            "start_date": "2018-10-06T00:00:00+00:00",
            "end_date": "2019-07-28T00:00:00+00:00",
            "score": 8.59,
            "rating": "R",
            "airing": False,
            "mal_id": 37991
        },
        {
            "title": "JoJo no Kimyou na Bouken Part 4: Diamond wa Kudakenai",
            "image": "https://cdn.myanimelist.net/images/anime/3/79156.jpg?s=9189ddeb4a702d774b985fe16f315a81",
            "synopsis": "The year is 1999. Morioh, a normally quiet and peaceful town, has recently become a hotbed of strange activity. Joutarou Kuujou, now a marine biologist, heads to the mysterious town to meet Jousuke Hi...",
            "type": "TV",
            "start_date": "2016-04-02T00:00:00+00:00",
            "end_date": "2016-12-24T00:00:00+00:00",
            "score": 8.5,
            "rating": "R",
            "airing": False,
            "mal_id": 31933
        }
    ]

    api_anime_data = {
        "request_hash": "request:anime:e09f711f9e532dc4ffb4d5649b5dbd6e0fb25648",
        "request_cached": True,
        "request_cache_expiry": 68876,
        "mal_id": 11061,
        "url": "https://myanimelist.net/anime/11061/Hunter_x_Hunter_2011",
        "image_url": "https://cdn.myanimelist.net/images/anime/11/33657.jpg",
        "trailer_url": "https://www.youtube.com/embed/D9iTQRB4XRk?enablejsapi=1&wmode=opaque&autoplay=1",
        "title": "Hunter x Hunter (2011)",
        "title_english": "Hunter x Hunter",
        "title_japanese": "HUNTER×HUNTER（ハンター×ハンター）",
        "title_synonyms": [
            "HxH (2011)"
        ],
        "type": "TV",
        "source": "Manga",
        "episodes": 148,
        "status": "Finished Airing",
        "airing": None,
        "aired": {
            "from": "2011-10-02T00:00:00+00:00",
            "to": "2014-09-24T00:00:00+00:00",
            "prop": {
                "from": {
                    "day": 2,
                    "month": 10,
                    "year": 2011
                },
                "to": {
                    "day": 24,
                    "month": 9,
                    "year": 2014
                }
            },
            "string": "Oct 2, 2011 to Sep 24, 2014"
        },
        "duration": "23 min per ep",
        "rating": "PG-13 - Teens 13 or older",
        "score": 9.07,
        "scored_by": 1119222,
        "rank": 5,
        "popularity": 11,
        "members": 1927851,
        "favorites": 149264,
        "synopsis": "Hunter x Hunter is set in a world where Hunters exist to perform all manner of dangerous tasks like capturing criminals and bravely searching for lost treasures in uncharted territories. Twelve-year-old Gon Freecss is determined to become the best Hunter possible in hopes of finding his father, who was a Hunter himself and had long ago abandoned his young son. However, Gon soon realizes the path to achieving his goals is far more challenging than he could have ever imagined. Along the way to becoming an official Hunter, Gon befriends the lively doctor-in-training Leorio, vengeful Kurapika, and rebellious ex-assassin Killua. To attain their own goals and desires, together the four of them take the Hunter Exam, notorious for its low success rate and high probability of death. Throughout their journey, Gon and his friends embark on an adventure that puts them through many hardships and struggles. They will meet a plethora of monsters, creatures, and characters—all while learning what being a Hunter truly means. [Written by MAL Rewrite]",
        "background": None,
        "premiered": "Fall 2011",
        "broadcast": "Sundays at 10:55 (JST)",
        "related": {
            "Adaptation": [
                {
                    "mal_id": 26,
                    "type": "manga",
                    "name": "Hunter x Hunter",
                    "url": "https://myanimelist.net/manga/26/Hunter_x_Hunter"
                }
            ],
            "Alternative version": [
                {
                    "mal_id": 136,
                    "type": "anime",
                    "name": "Hunter x Hunter",
                    "url": "https://myanimelist.net/anime/136/Hunter_x_Hunter"
                },
                {
                    "mal_id": 137,
                    "type": "anime",
                    "name": "Hunter x Hunter: Original Video Animation",
                    "url": "https://myanimelist.net/anime/137/Hunter_x_Hunter__Original_Video_Animation"
                },
                {
                    "mal_id": 138,
                    "type": "anime",
                    "name": "Hunter x Hunter: Greed Island",
                    "url": "https://myanimelist.net/anime/138/Hunter_x_Hunter__Greed_Island"
                },
                {
                    "mal_id": 139,
                    "type": "anime",
                    "name": "Hunter x Hunter: Greed Island Final",
                    "url": "https://myanimelist.net/anime/139/Hunter_x_Hunter__Greed_Island_Final"
                }
            ],
            "Side story": [
                {
                    "mal_id": 13271,
                    "type": "anime",
                    "name": "Hunter x Hunter Movie 1: Phantom Rouge",
                    "url": "https://myanimelist.net/anime/13271/Hunter_x_Hunter_Movie_1__Phantom_Rouge"
                },
                {
                    "mal_id": 19951,
                    "type": "anime",
                    "name": "Hunter x Hunter Movie 2: The Last Mission",
                    "url": "https://myanimelist.net/anime/19951/Hunter_x_Hunter_Movie_2__The_Last_Mission"
                }
            ]
        },
        "producers": [
            {
                "mal_id": 29,
                "type": "anime",
                "name": "VAP",
                "url": "https://myanimelist.net/anime/producer/29/VAP"
            },
            {
                "mal_id": 1003,
                "type": "anime",
                "name": "Nippon Television Network",
                "url": "https://myanimelist.net/anime/producer/1003/Nippon_Television_Network"
            },
            {
                "mal_id": 1365,
                "type": "anime",
                "name": "Shueisha",
                "url": "https://myanimelist.net/anime/producer/1365/Shueisha"
            }
        ],
        "licensors": [
            {
                "mal_id": 119,
                "type": "anime",
                "name": "VIZ Media",
                "url": "https://myanimelist.net/anime/producer/119/VIZ_Media"
            }
        ],
        "studios": [
            {
                "mal_id": 11,
                "type": "anime",
                "name": "Madhouse",
                "url": "https://myanimelist.net/anime/producer/11/Madhouse"
            }
        ],
        "genres": [
            {
                "mal_id": 1,
                "type": "anime",
                "name": "Action",
                "url": "https://myanimelist.net/anime/genre/1/Action"
            },
            {
                "mal_id": 2,
                "type": "anime",
                "name": "Adventure",
                "url": "https://myanimelist.net/anime/genre/2/Adventure"
            },
            {
                "mal_id": 10,
                "type": "anime",
                "name": "Fantasy",
                "url": "https://myanimelist.net/anime/genre/10/Fantasy"
            },
            {
                "mal_id": 27,
                "type": "anime",
                "name": "Shounen",
                "url": "https://myanimelist.net/anime/genre/27/Shounen"
            },
            {
                "mal_id": 31,
                "type": "anime",
                "name": "Super Power",
                "url": "https://myanimelist.net/anime/genre/31/Super_Power"
            }
        ],
        "opening_themes": [
            "#1: \"departure!\" by Ono Masatoshi (eps 1-26, 50-52, 62-75, 137-147)",
            "#2: \"departure! -second version-\" by Ono Masatoshi (eps 27-49, 76-103, 109-134, 136)",
            "#3: \"departure! -Opening Tokubetsu-hen-\" by Ono Masatoshi (eps 53-61, 104-108)"
        ],
        "ending_themes": [
            "#1: \"Just Awake\" by Fear, and Loathing in Las Vegas (eps 1-26)",
            "#2: \"HUNTING FOR YOUR DREAM\" by Galneryus (eps 27-50, 52-58)",
            "#3: \"Riot\" by Yoshihisa Hirano (ep 51)",
            "#4: \"REASON\" by YUZU (ゆず) (eps 59-75, 147)",
            "#5: \"Nagareboshi Kirari (YUZU Version) (流れ星キラリ (ゆずバージョン))\" by YUZU (ゆず) (eps 76-98)",
            "#6: \"Hyouriittai (表裏一体)\" by YUZU (ゆず) (eps 99-134, 136)",
            "#7: \"Understanding\" by Yoshihisa Hirano (ep 135)",
            "#8: \"Hyouriittai -second version- (表裏一体)\" by YUZU (ゆず) (eps 137-146)",
            "#9: \"departure!\" by Ono Masatoshi (ep 148)"
        ]
    }
    ###############SETUP###############

    def setUp(self) -> None:
        main.app.testing = True
        self.app = main.app.test_client()

    ###############GET ENDPOINT###############
    def test_anime_endpoint_get_correct_1(self):
        response = self.app.get(
            f"/anime/{self.get_data[0]['mal_id']}")
        self.assertIn(response.status, ['200 OK', '429 Too Many Requests', '500 Internal Server Error',
                      '502 Bad Gateway', '503 Service Unavailable', '504 Gateway Timeout'])
        if response.status == 200:
            self.assertEqual(response.json, {
                self.get_data[0]
            })

    def test_anime_get_correct_2(self):
        response = self.app.get(
            f"/anime/{self.get_data[1]['mal_id']}")
        self.assertIn(response.status, ['200 OK', '429 Too Many Requests', '500 Internal Server Error',
                      '502 Bad Gateway', '503 Service Unavailable', '504 Gateway Timeout'])
        if response.status == 200:
            self.assertEqual(response.json, {
                self.get_data[1]
            })

    def test_anime_get_wrong_1(self):
        response = self.app.get(
            "/anime/"
        )
        self.assertEqual(response.status, '405 METHOD NOT ALLOWED')

    def test_anime_get_wrong_2(self):
        response = self.app.get(
            "/anime/string"
        )
        self.assertEqual(response.status, '404 NOT FOUND')

    def test_anime_get_wrong_3(self):
        response = self.app.get(
            "/anime/0"
        )
        self.assertEqual(response.status, '404 NOT FOUND')

    ###############SEARCH ENDPOINT###############
    def test_anime_search_correct_1(self):
        response = self.app.get(
            f"/anime/search/?query={self.search_query}&limit={self.seach_limit}"
        )
        self.assertIn(response.status, ['200 OK', '429 Too Many Requests', '500 Internal Server Error',
                      '502 Bad Gateway', '503 Service Unavailable', '504 Gateway Timeout'])
        if response.status == 200:
            self.assertEqual(
                response.json,
                self.search_data
            )

    def test_anime_search_correct_2(self):
        response = self.app.get(
            f"/anime/search/?query={self.search_query}&limit=1"
        )
        self.assertIn(response.status, ['200 OK', '429 Too Many Requests', '500 Internal Server Error',
                                        '502 Bad Gateway', '503 Service Unavailable', '504 Gateway Timeout'])
        if response.status == 200:
            self.assertEqual(
                response.json,
                self.search_data[:1]
            )

    def test_anime_search_wrong_1(self):
        response = self.app.get(
            f"/anime/search/"
        )
        self.assertEquals(
            response.status, '400 BAD REQUEST'
        )
        self.assertEquals(
            response.data, b'Please enter a query string'
        )

    def test_anime_search_wrong_2(self):
        response = self.app.get(
            f"/anime/search/?query=jojo"
        )
        self.assertEquals(
            response.status, '400 BAD REQUEST'
        )
        self.assertEquals(
            response.data, b'Please enter a valid integer as your limit.'
        )

    def test_anime_search_wrong_3(self):
        response = self.app.get(
            f"/anime/search/?query=jo"
        )
        self.assertEquals(
            response.status, '400 BAD REQUEST'
        )
        self.assertEquals(
            response.data, b'Query string must be at least 3 characters long'
        )

    def test_anime_search_wrong_4(self):
        response = self.app.get(
            f"/anime/search/?query=jojo&limit=string"
        )
        self.assertEquals(
            response.status, '400 BAD REQUEST'
        )
        self.assertEquals(
            response.data, b'Please enter a valid integer as your limit.'
        )

    ###############POST ENDPOINT###############
    def test_anime_post_correct(self):
        response = self.app.post("/anime/", json=self.randomAnimeGenerator())
        self.assertEquals(response.status, '200 OK')

    def test_anime_post_wrong_1(self):
        body = self.randomAnimeGenerator()
        del body["end_date"]
        response = self.app.post("/anime/", json=body)
        self.assertEquals(response.status, '400 BAD REQUEST')
        self.assertEquals(response.data, b"Body must have end_date field.")

    def test_anime_post_wrong_2(self):
        body = self.randomAnimeGenerator()
        body["episodes"] = "string"
        response = self.app.post("/anime/", json=body)
        self.assertEquals(response.status, '403 FORBIDDEN')
        self.assertEquals(response.data.decode(
            'utf8'), '{"err": "Validation", "errs": [{"field": "episodes", "msg": "Please enter a valid integer"}]}')

    def test_anime_post_wrong_3(self):
        body = self.randomAnimeGenerator()
        self.app.post("/anime/", json=body)
        response = self.app.post("/anime/", json=body)
        self.assertEquals(response.status, '403 FORBIDDEN')
        self.assertEquals(
            response.data, b"UNIQUE constraint failed: UserAnimes.title, UserAnimes.type")

    ###############HELPER###############
    def test_validate_search_params_correct(self):
        params = {"query": "jojo", "limit": "3"}
        result = anime_helper.validate_search_params(params)
        self.assertIsNone(result)

    def test_validate_search_params_wrong_1(self):
        params = {"limit": 3}
        result = anime_helper.validate_search_params(params)
        self.assertEquals(result.status, '400 BAD REQUEST')
        self.assertEquals(result.data, b"Please enter a query string")

    def test_validate_search_params_wrong_2(self):
        params = {"query": "jojo"}
        result = anime_helper.validate_search_params(params)
        self.assertEquals(result.status, '400 BAD REQUEST')
        self.assertEquals(
            result.data, b"Please enter a valid integer as your limit.")

    def test_validate_search_params_wrong_3(self):
        params = {"query": "jo", "limit": 3}
        result = anime_helper.validate_search_params(params)
        self.assertEquals(result.status, '400 BAD REQUEST')
        self.assertEquals(
            result.data, b"Query string must be at least 3 characters long")

    def test_validate_search_params_wrong_4(self):
        params = {"query": "jojo", "limit": "string"}
        result = anime_helper.validate_search_params(params)
        self.assertEquals(result.status, '400 BAD REQUEST')
        self.assertEquals(
            result.data, b"Please enter a valid integer as your limit.")

    def test_validate_and_map_user_anime_correct(self):
        body = self.randomAnimeGenerator()
        result = anime_helper.validate_and_map_user_anime(body)
        self.assertEquals(result, create_anime_mapper(body))

    def test_validate_and_map_user_anime_wrong_1(self):
        body = self.randomAnimeGenerator()
        del body["end_date"]
        response = anime_helper.validate_and_map_user_anime(body)
        self.assertEquals(response.status, '400 BAD REQUEST')
        self.assertEquals(response.data, b"Body must have end_date field.")

    def test_validate_and_map_user_anime_wrong_2(self):
        body = self.randomAnimeGenerator()
        body["episodes"] = "string"
        response = anime_helper.validate_and_map_user_anime(body)
        self.assertEquals(response.status, '403 FORBIDDEN')
        self.assertEquals(response.data.decode(
            'utf8'), '{"err": "Validation", "errs": [{"field": "episodes", "msg": "Please enter a valid integer"}]}')

    def test_add_mal_anime_to_db(self):
        anime_helper.add_mal_anime_to_db(self.api_anime_data)
        con = sqlite3.connect("/usr/src/app/./sqlfiles/practice-app.db")
        cur = con.cursor()
        data = cur.execute("SELECT * FROM MalAnimes WHERE title='Hunter x Hunter (2011)' and mal_id=11061")
        anime = data.fetchall()[0]
        self.assertEquals(anime[3], self.api_anime_data["episodes"])
        self.assertEquals(anime[10], self.api_anime_data["type"])

    def test_add_user_anime_to_db(self):
        body = self.randomAnimeGenerator()
        anime_helper.add_user_anime_to_db(body)
        con = sqlite3.connect("/usr/src/app/./sqlfiles/practice-app.db")
        cur = con.cursor()
        data = cur.execute("SELECT * FROM UserAnimes WHERE title = (?) AND type = (?)", (body["title"], body["type"]))
        anime = data.fetchall()[0]
        self.assertEquals(anime[2], body["episodes"])
        self.assertEquals(anime[8], body["rating"])
        
    def test_add_user_anime_to_db_wrong(self):
        body = self.randomAnimeGenerator()
        anime_helper.add_user_anime_to_db(body)
        response = anime_helper.add_user_anime_to_db(body)
        self.assertEquals(response.status, '403 FORBIDDEN')
        self.assertEquals(
            response.data, b"UNIQUE constraint failed: UserAnimes.title, UserAnimes.type")

    def tearDown(self):
        pass

if __name__ == '__main__':
    # create a runner to see the output test reports
    root_dir = os.path.dirname(__file__)
    test_loader = unittest.TestLoader()
    package_tests = test_loader.discover(start_dir=root_dir)
    #  runner = HTMLTestRunner(combine_reports=True, report_name="MyReport", add_timestamp=False)
    runner = HTMLTestRunner(combine_reports=True, output='./reports/html/', report_name="Anime-API-Tests-Report",
                            add_timestamp=True)
    unittest.main(testRunner=runner)

