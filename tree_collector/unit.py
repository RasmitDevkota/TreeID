from os import path

count = 221

dir = "../app/src/main/res/drawable/"

for i in range(0, 221):
    fn = f"{dir}nyssa_sylvatica_{i}.jpg"
    if not path.exists(fn):
        print(fn)
