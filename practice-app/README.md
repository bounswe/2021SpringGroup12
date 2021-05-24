To gain experience with development of a project as a team, we will build a practice app here.


Here we have an API, written using The Flask framework.

* Create a virtual environment: `virtualenv myenv`
* Activate the virtual environment: 
  * Windows: `myenv\Scripts\activate`
  * Linux / MacOS:  `source myenv/bin/activate`
* Install the requirements: `pip install -r requirements.txt`
* Execute the API:
  * Windows:
    * Powershell:
      * `$env:FLASK_APP = "practice-app"`
      * `python -m flask run`
    * cmd: 
      * `set FLASK_APP=practice-app.py`
      * `set FLASK_ENV=development`
      * `flask run`
  * Linux / MacOS:
    * `export FLASK_APP=practice-app.py`
    * `export FLASK_ENV=development`
    * `flask run` 
* Go to the browser and open that link just you seen in the terminal.  (likely http://127.0.0.1:5000/)
* Clone the repo to your local and branch out. 
* Write your own endpoints using different existing API's.
* Test your endpoints using Postman. Try to use query-path parameters, request bodies, etc. 
