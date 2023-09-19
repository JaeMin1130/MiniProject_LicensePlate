import React, { useState, useEffect, useRef } from 'react';
import { Card, CardMedia } from '@mui/material';
import './Slideshow.css';

const imagesData = [];
const uniqueIds = Array.from({ length: 30 }, (_, index) => index +1 );

for (let i = uniqueIds.length - 1; i >= 0; i--) {
  const j = Math.floor(Math.random() * (i + 1));
  [uniqueIds[i], uniqueIds[j]] = [uniqueIds[j], uniqueIds[i]];
}

for (let i = 1; i <= 30; i++) {
  const randomI = uniqueIds[i - 1]; 
  const image = { id: randomI, src: `https://licenseplate-iru.s3.ap-northeast-2.amazonaws.com/sample/img${randomI}.jpg` };
  imagesData.push(image);
}


export default function Images({ showRecord }) {
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const imageRef = useRef(null); 
  const currentImage = imagesData[currentImageIndex];
  
  useEffect(() => {

    const interval = setInterval(() => {
      setCurrentImageIndex((prevIndex) => (prevIndex + 1) % imagesData.length);
    }, 15000);
    return () => {
      if (currentImage) {
        const src = imageRef.current ? imageRef.current.src : '';
        loadImage(src);
      }
      clearInterval(interval);
    };
  }, [currentImageIndex]);


  const loadImage = async (imageSrc) => {
    try {
      const response = await fetch(imageSrc);
      if (response.ok) {
        const blob = await response.blob();
        showRecord(blob);
      } else {
        console.error('Failed to load image');
      }
    } catch (error) {
      console.error('Error loading image:', error);
    }
  };

  return (
    <div className="slideshow-container">
      {currentImage && (
        <Card  style={{ height: 'fit-content', width: '100%' }}>
          <CardMedia
            component="img"
            alt="Displayed Image"
            src={currentImage.src}
            ref={imageRef} 
            style={{ height: '50vh', width: '100%', objectFit: 'cover' }}
          />
        </Card>
      )}
    </div>
  );
}
