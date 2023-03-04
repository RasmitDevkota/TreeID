import os
import json
import requests

trees_file = open("./trees.txt", "r+")

trees_full = [line.replace("\n", "").split(",") for line in trees_file.readlines()]
trees = [line[1] for line in trees_full]

trees_file.close()

start = False

visited_ids = []
for t in range(len(trees)):
    tree = trees[t]
    tree_name = tree.replace(" ", "_")

    if os.path.isdir(f"./images/{tree_name}") and tree != "larix occidentalis":
        continue

    print(f"tree: {tree}")

    current_ids = []

    directory = f"./images/{tree_name}/"
    os.makedirs(directory, exist_ok=True)

    query_results = json.loads(requests.get(f"https://api.inaturalist.org/v1/observations?photos=true&photo_licensed=true&rank=species&taxon_name={tree}&quality_grade=research&per_page=500&order_by=id&order=asc").content)

    for obs in query_results["results"]:
        for photo in obs["photos"]:
            image_id = photo["id"]

            if image_id not in visited_ids and not os.path.isfile(f"{directory}{tree_name}_{image_id}.jpg"):
                visited_ids.append(image_id)

                image_url = f"https://inaturalist-open-data.s3.amazonaws.com/photos/{image_id}/medium.jpg"

                image_data = requests.get(image_url).content

                image_file = open(f"{directory}{tree_name}_{image_id}.jpg", "wb")
                image_file.write(image_data)
                image_file.close()
