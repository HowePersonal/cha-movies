import styled from 'styled-components'

export const SiteStyle = styled.div`
   font-family: 'Roboto', sans-serif;
   a {
      color: #007BFF;           
      text-decoration: none;    
      transition: color 0.3s;  
      font-weight: 500;         
      padding-bottom: 2px;      
  
   }
`;

export const NavbarStyle = styled.div`
   display: flex;
   align-items: center;
   justify-content: space-between;
   box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
   padding: 10px 20px;
   background-color: #ffffff;

   a {
      white-space: nowrap;
      color: black;
      font-weight: bold;
   }

   .homeLink {
      font-size: 30px;
      font-weight: bold;
      color: black;

      &:hover {
         color: #333;
      }
   }

   form {
      display: flex;
      justify-content:center;
      align-items: center;
      font-size: 14px;
      gap: 5px;
      padding: 5px;
      width: 60%;
      border-radius: 7px;


      input[type="text"] {
         border: none;
         height: 20px;
         width: 150px;
         width: 80px;
         padding: 2px 23px 2px 3px;
         outline: 0;
         background-color: #f5f5f5;
      }

      input[type="submit"] {
         background-color: #D3D3D3;
         height: 22.5px;
         border: none;

         cursor: pointer;
         font-size: 13px;
         text-align: center;
         transition: background-color 0.3s;
      }
   }



   form input:hover {
      background-color: #D3D3D3;
  }
`

