import React, { useCallback, useState } from "react";
import { useDropzone } from "react-dropzone";
import {
  Box,
  Fab,
  IconButton,
  Typography,
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";
import ClearIcon from "@mui/icons-material/Clear";


export default function ImageLoader() {
  const [uploadPreview, setUploadPreview] = useState([]);
  const [currentPhotoIndex, setCurrentPhotoIndex] = useState(0);
  const [imageUpload, setImageUpload] = useState([]); 

  const handleImageCompress = async (file) => {
  };

  const handleNextPhoto = () => {
    setCurrentPhotoIndex((prevIndex) => (prevIndex + 1) % imageUpload.length);
  };

  const handlePreviousPhoto = () => {
    setCurrentPhotoIndex((prevIndex) =>
      (prevIndex - 1 + imageUpload.length) % imageUpload.length
    );
  };

  const handleDeletePhoto = (index, event) => {
    event.stopPropagation();
    setImageUpload((prevFiles) => prevFiles.filter((_, i) => i !== index)); 
  };

  const handleDrop = useCallback(
    async (acceptedFiles) => {
      if (acceptedFiles && acceptedFiles.length > 0) {
        const compressedFiles = await Promise.all(
          acceptedFiles.map((file) => handleImageCompress(file))
        );
        const validCompressedFiles = compressedFiles.filter(
          (file) => file !== null
        );
        setImageUpload((prevFiles) => [...prevFiles, ...validCompressedFiles]); 
        setUploadPreview(
          validCompressedFiles.map((file) => URL.createObjectURL(file))
        );
      }
    },
    [handleImageCompress]
  );

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop: handleDrop,
  });

  return (
    <Box sx={{ display: "flex", alignContent: "center", justifyContent: "space-evenly", mt: 3 }}>
      <IconButton onClick={handlePreviousPhoto}>
        <ArrowBackIosNewIcon />
      </IconButton>
      <Box
        {...getRootProps()}
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          width: "73%",
          aspectRatio: "1 / 1",
          border: isDragActive ? "2px solid tomato" : "2px solid grey",
          borderRadius: "5px",
          position: "relative",
        }}
      >
        {imageUpload.length > 0 ? (
          <Box
            sx={{
              width: "100%",
              height: "100%",
              position: "relative",
              borderRadius: "8px",
              overflow: "hidden",
            }}
          >
            <img
              src={URL.createObjectURL(imageUpload[currentPhotoIndex])}
              alt="Uploaded"
              style={{
                width: "100%",
                height: "100%",
                transition: "opacity 0.5s",
              }}
            />
            {imageUpload.map((file, index) => (
              <IconButton
                key={index}
                onClick={(event) => handleDeletePhoto(index, event)} 
                sx={{
                  position: "absolute",
                  right: -5,
                  top: -5,
                }}
              >
                <Fab
                  color="primary"
                  size="small"
                  sx={{ bgcolor: "transparent", width: "30px", height: 0, borderRadius: "20%" }}
                >
                  <ClearIcon fontSize="small" />
                </Fab>
              </IconButton>
            ))}
          </Box>
        ) : (
          <Typography variant="h5" alignSelf={"center"}>
            {isDragActive ? "Drop the Photo here." : "Drag a photo here."}
          </Typography>
        )}
        <IconButton
          sx={{
            position: "absolute",
            right: 1,
            bottom: 1,
          }}
        >
          <Fab
            color="primary"
            size="small"
            sx={{
              bgcolor: imageUpload.length > 0 ? "transparent" : "gray",
              width: "30px",
              height: 0,
            }}
          >
            <AddIcon fontSize="small" />
          </Fab>
        </IconButton>
      </Box>
      <IconButton onClick={handleNextPhoto}>
        <ArrowForwardIosIcon />
      </IconButton>
    </Box>
  );
}
