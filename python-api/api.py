from flask import Flask, request, jsonify
import subprocess
import os

app = Flask(__name__)

HLS_DIRECTORY = os.getenv("HLS_DIRECTORY", "/app/hls_storage")

@app.route('/generate-thumbnail', methods=['POST'])
def generate_thumbnail():
    data = request.get_json()
    key = data.get('key')

    if not key:
        return jsonify({"error": "Missing key"}), 400

    key = str(key)

    # Construct the input and output file paths
    input_file = os.path.join(HLS_DIRECTORY, key, f"{key}.m3u8")
    output_file = os.path.join(HLS_DIRECTORY, key, "thumbnail.jpg")

    if not os.path.exists(input_file):
        return jsonify({"error": "Stream file not found"}), 404

    try:
        # Run ffmpeg to generate the thumbnail
        subprocess.run(
            ["ffmpeg", "-y", "-nostdin", "-i", input_file, "-vf", "thumbnail", "-frames:v", "1", "-pix_fmt", "yuv420p", output_file],
            check=True
        )
        # Print success message
        print(f"Thumbnail successfully generated at {output_file}")
        return jsonify({"message": "Thumbnail generated", "path": output_file}), 200
    except subprocess.CalledProcessError as e:
        # Print error message if ffmpeg fails
        print(f"Error during thumbnail generation: {str(e)}")
        return jsonify({"error": str(e)}), 500


if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000)
