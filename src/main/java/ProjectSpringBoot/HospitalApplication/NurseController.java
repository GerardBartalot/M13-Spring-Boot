package ProjectSpringBoot.HospitalApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/nurse")
public class NurseController {

	@Autowired
	private NurseRepository nurseRepository;

	@PostMapping("/create")
	public ResponseEntity<String> createNurse(@RequestBody Nurse newNurse) {
	    if (newNurse.getName() == null || newNurse.getName().isEmpty() ||
	        newNurse.getUsername() == null || newNurse.getUsername().isEmpty() ||
	        newNurse.getPassword() == null || newNurse.getPassword().isEmpty()) {
        
	        return new ResponseEntity<>("All fields (id, name, username, and password) are required.", HttpStatus.BAD_REQUEST);
	    }
	    	Nurse savedNurse = nurseRepository.save(newNurse);

	    if (savedNurse != null) {
	        return new ResponseEntity<>("Nurse added successfully with ID: " + savedNurse.getNurse_id(), HttpStatus.CREATED);
	    } else {
	        return new ResponseEntity<>("Error creating nurse", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> validateLogin(@RequestParam String username, @RequestParam String password) {
	    Nurse nurse = nurseRepository.findByUsernameAndPassword(username, password); 

	    if (nurse != null) {
	        return new ResponseEntity<>("Login Successful", HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
	    }
	}
  
	@GetMapping("/index")
	public ResponseEntity<List<Nurse>> getAll() {
	    List<Nurse> nurses = (List<Nurse>) nurseRepository.findAll();
	    return ResponseEntity.ok(nurses); 
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<Nurse> findByName(@PathVariable String name) {
	    Nurse nurse = nurseRepository.findByName(name);
	    if (nurse != null && nurse.getName().equalsIgnoreCase(name)) {
	        return ResponseEntity.ok(nurse); // 200
	    }
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404
	}

	@PutMapping("/delete/{id}")
	public ResponseEntity<String> deleteNurse(@PathVariable int id) {
		Optional<Nurse> existingNurse = nurseRepository.findById(id);
		if (existingNurse.isPresent()) {
			this.nurseRepository.deleteById(id);
			return new ResponseEntity<>("Nurse deleted", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Id not found", HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("/read/{id}")
    	public ResponseEntity<Nurse> getNurseById(@PathVariable int id) {
        	Optional<Nurse> nurse = nurseRepository.findById(id);
        	if (nurse.isPresent()) {
           	        return new ResponseEntity<>(nurse.get(), HttpStatus.OK);//200
        	} else {
            		return new ResponseEntity<>(HttpStatus.NOT_FOUND);//404
       		}
    	}
  
	@PutMapping("/update/{id}")
	public ResponseEntity<String> updateNurse(@PathVariable int id, @RequestBody Nurse updatedNurse) {
	    Optional<Nurse> existingNurse = nurseRepository.findById(id);

	    if (existingNurse.isPresent()) {
	        Nurse nurse = existingNurse.get();

	        if (updatedNurse.getName() != null) {
	            nurse.setName(updatedNurse.getName());
	        }
	        if (updatedNurse.getUsername() != null) {
	            nurse.setUsername(updatedNurse.getUsername());
	        }
	        if (updatedNurse.getPassword() != null) {
	            nurse.setPassword(updatedNurse.getPassword());
	        }

	        nurseRepository.save(nurse);
	        return new ResponseEntity<>("Nurse updated successfully", HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>("Nurse not found", HttpStatus.NOT_FOUND);
	    }
	}
  
}
