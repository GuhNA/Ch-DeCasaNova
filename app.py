import sys
from os import environ

from flask import Flask, request, jsonify, make_response
from flask_sqlalchemy import SQLAlchemy


app = Flask(__name__)
app.config["SQLALCHEMY_DATABASE_URI"] = environ.get("DB_URL")
db = SQLAlchemy(app)


class User(db.Model):
    __tablename__ = "users"

    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(80), unique=True, nullable=False)

    def json(self):
        return {"id": self.id, "username": self.username}


with app.app_context():
    db.create_all()


@app.route("/test", methods=["GET"])
def test():
    return make_response(jsonify({"message": "test_route"}), 200)


@app.route("/user", methods=["POST"])
def create_user():
    try:
        data = request.get_json()
        new_user = User(
            username=data["username"],
        )
        db.session.add(new_user)
        db.session.commit()
        return make_response(jsonify({"message": "user created!"}), 201)
    except Exception as err:
        print(err, file=sys.stderr)
        return make_response(jsonify({"message": "error creating user"}), 500)


@app.route("/user", methods=["GET"])
def get_users():
    try:
        users = User.query.all()
        if len(users):
            return make_response(
                jsonify({"users": [user.json() for user in users]}), 200
            )
        return make_response(jsonify({"message": "no user found!"}), 404)
    except Exception as err:
        print(err, file=sys.stderr)
        return make_response(jsonify({"message": "error while fetching users"}), 500)
