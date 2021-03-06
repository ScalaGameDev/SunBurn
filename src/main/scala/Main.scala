/*                  ______                  
**                  | ___ \                 
**   ___ _   _ _ __ | |_/ /_   _ _ __ _ __  
**  / __| | | | '_ \| ___ \ | | | '__| '_ \ 
**  \__ \ |_| | | | | |_/ / |_| | |  | | | |
**  |___/\__,_|_| |_\____/ \__,_|_|  |_| |_|
**
**             SunBurn RayTracer
**        http://www.hsyl20.fr/sunburn
**                   GPLv3
*/

package fr.hsyl20.sunburn

import fr.hsyl20.sunburn.geometry._
import fr.hsyl20.sunburn.geometry.Vector3D._
import fr.hsyl20.sunburn.core._

import fr.hsyl20.sunburn.samplers._
import fr.hsyl20.sunburn.colors._
import fr.hsyl20.sunburn.colors.RGBColor._
import fr.hsyl20.sunburn.materials._
import fr.hsyl20.sunburn.cameras._
import fr.hsyl20.sunburn.lights._

import scala.actors.Futures._

object Main {

  def main(args: Array[String]) = {

     val myworld = new World() {
        objects (
            new Sphere {
                center = (0.0, 12.0, 0.0)
                radius = 10.0
                material = new MatteMaterial {
                    color = Yellow
                    diffuse = 0.5
                    ambient = 0.5
                }
            },

            new Sphere {
                center = (20.0, 10.0, 0.0)
                radius = 5.0
                material = new MatteMaterial {
                    color = Red
                    diffuse = 1
                    ambient = 1
                }
            },

            new Plane {
                point = Point3D(0)
                normal = Point3D(0, 1, 0)
                material = new MatteMaterial {
                    color = RGBColor(0.2,0.2,0.2)
                    diffuse = 2.2
                    ambient = 2.2
                }
            }
        )
    
        backgroundColor = Green
        
        ambientLight = new SimpleAmbientLight {
            intensity = 0.2
            color = Blue
        }

        lights (
            new PointLight {
                intensity = 8.0
                color = White
                location = Point3D(10, 30, 10)
            }
        )
     }
    

    val vp = new ViewPlane {
        size = (800,600)
        resolution = 0.2
        sampler = new BufferedSampler(new ShuffledSampler(new MultiJitteredSampler(16)))
    }

    val c = PinholeCamera (
        world = myworld,
        viewPlane = vp,
        eye = Point3D(0,25,20),
        lookat = Point3D(0,15,0),
        viewDistance = 50.0
    )

    val c2 = ThinLensCamera (
        world = myworld,
        viewPlane = vp,
        eye = Point3D(0,40,40),
        lookat = Point3D(0,0,0),
        viewDistance = 50.0,
        focalDistance = 40.0,
        lensRadius = 2.0,
        sampler = new RotationDiscSampler(new ShuffledSampler(new MultiJitteredSampler(16)))
    )

    /*BUGGY (see class file)
    val c = OrthographicCamera(
        world = myworld,
        viewPlane = vp,
        eye =  Point3D(0,40,40),
        lookat = Point3D(0,0,0)
    )*/

    //val r2 = new Displayer(c2)
    //future(c2.render)
    
    val r = new Displayer(c)
    c.render


    ()
  }

}
