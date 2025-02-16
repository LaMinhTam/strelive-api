FROM python:3.9

# Install FFmpeg
RUN apt-get update && apt-get install -y ffmpeg

# Set the working directory
WORKDIR /app

# Copy requirements and install dependencies (if needed)
#COPY requirements.txt .
#RUN pip install -r requirements.txt
RUN pip install flask
# Copy the API script
COPY api.py .

# Expose the port Flask will run on
EXPOSE 5000

# Run the Flask app
CMD ["python", "api.py"]
