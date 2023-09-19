import React, { useState, useEffect } from "react";
import Modal from "react-modal";
import Draggable from "react-draggable";
import "./ImageModal.css";

const ImageModal = ({ isOpen, imageUrl, onClose }) => {
  const [showImage, setShowImage] = useState(false);
  const [imageWidth, setImageWidth] = useState(300);
  const [brightness, setBrightness] = useState(100);

  useEffect(() => {
   
    const img = new Image();
    img.src = imageUrl;
    img.onload = () => {
      setImageWidth(img.width);
    };
  }, [imageUrl]);

  const toggleImage = () => {
    setShowImage(!showImage);
  };

  const customStyles = {
    overlay: {
      display: "flex",
      alignItems: "center",
      justifyContent: "center",
      zIndex: 9999,
    },
    content: {
      position: "relative",
      zIndex: 10000,
      border: "none", 
      background: "none",
      overflow: "visible", 
      width: `${imageWidth}px`, 
    },
  };
  
  const handleBrightnessChange = (event) => {
    setBrightness(event.target.value);
  };

  const imageStyle = {
    filter: `brightness(${brightness}%)`,
  };

  
  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onClose}
      contentLabel="Image Modal"
      style={customStyles}
    >
      <Draggable handle=".drag-handle">
        <div>
          <div className="drag-handle">
            <span>이미지 이동</span> 
            <div className="close-button-container">
              <button className="close-button" onClick={onClose}>
                X
              </button>
              </div>
          </div>
          <div className="image-container">
            {showImage ? (
              <img src={imageUrl} alt="Full Image" style={imageStyle} />
            ) : (
              <div className="emoticon" onClick={toggleImage}>
                {toggleImage()}
              </div>
            )}
          </div>
          <div className="adjustment-controls">
            <div>
              <label htmlFor="brightness">밝기</label>
              <input
                type="range"
                id="brightness"
                min="0"
                max="200"
                value={brightness}
                onChange={handleBrightnessChange}
              />
               <span>{brightness}%</span> 
            </div>
          </div>
          <button onClick={onClose}>닫기</button>
        </div>
      </Draggable>
    </Modal>
  );
};

export default ImageModal;
