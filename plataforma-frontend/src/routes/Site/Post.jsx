import React from 'react';
import { useParams } from 'react-router-dom';
import MenuNavbar from "../../components/site/MenuNavbar/MenuNavbar.jsx";
import BreadCrumbs from "../../components/site/BreadCrumbs/BreadCrumbs.jsx";
import Footer from "../../components/site/Footer/Footer.jsx";
import PostContent from "../../components/site/PostContent/PostContent.jsx";


const barraCrumbs = [
  {
    type: "a",
    name: "Home", 
    ref: "/"
  },
  {
    type: "a",
    name: "News", 
    ref: "/news"
  },
  {
    type: "a",
    name: "Post", 
    ref: ""
  },
];

const Post = () => {
  const { postId } = useParams();

  return (
    <div>
      <MenuNavbar />
      
      <div className='h-screen bg-gray-900 p-2'>
        <div className="p-2 w-full">
          <BreadCrumbs itensCrumbs={barraCrumbs}/>
        </div>


          <PostContent postId={postId} />

      </div>
      <Footer />
    </div>
  );
};

export default Post;
